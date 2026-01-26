import java.util.*;

/**
 * Exact (non-sampling) expected weirdness score under independent per-cell distributions.
 *
 * weights[f][i][v] = weight for frame f, lamp i, value v (0..radix[i]-1)
 */
public class ExactWeirdExpectation {

    // A..P = 0..15
    public static final int A=0,B=1,C=2,D=3,E=4,F=5,G=6,H=7,I=8,J=9,K=10,L=11,M=12,N=13,O=14,P=15;
    public static final int TYPE_COUNT = 16;

    /** 你現有的 Config（越怪越高分）用這份即可；若你已經有同名Config，欄位對上就行 */
    public static class Config {
        public double epsSame = 0.10;
        public double brightTh = 0.90;
        public double smallDeltaTh = 0.30;

        public int bigChangeTh = 4;
        public double sideBiasTh = 1.0;

        public double mirrorRatioTh = 0.85;

        public double meanULowTh  = 0.15;
        public double meanUHighTh = 0.85;
        public double stripeCorrTh = 0.85;
        public double symDiffAvgTh = 0.08;
        public double varHighTh = 0.18;

        public double magPerDotTh = 0.55;
        public double maxDeltaTh = 0.80;
        public int lrDiffTh = 2;
        public int maxChangedTh = 4;

        public int lambda = 1;

        public boolean[] enabled = new boolean[TYPE_COUNT];
        public boolean enableMirrorBonus = true;

        public int[] score = new int[TYPE_COUNT];

        public Config() {
            Arrays.fill(enabled, true);
            Arrays.fill(score, 0);

            // 你可自行調整：越怪越高分
            score[A]=95; score[B]=90; score[C]=75; score[D]=88; score[E]=55; score[F]=60; score[G]=98;
            score[H]=35; score[I]=50; score[J]=45; score[K]=30; score[L]=55;
            score[M]=70; score[N]=65; score[O]=55; score[P]=80;
        }
    }

    /** 輸出：總期望 + 各type期望貢獻 */
    public static class Result {
        public final double expectedTotal;
        public final double[] expectedByType; // A..P
        public Result(double expectedTotal, double[] expectedByType) {
            this.expectedTotal = expectedTotal;
            this.expectedByType = expectedByType;
        }
    }

    // ------------------ public API ------------------

    public static Result expectedScoreExact(int[] radix, double[][][] weights, Config cfg) {
        int Fm = weights.length;
        int Mlen = radix.length;

        // normalize weights -> probabilities, and precompute u(v) as double for each i,v
        double[][][] p = normalize(weights, radix);

        double[] expType = new double[TYPE_COUNT];

        // ---- per-frame contributions (A,B,C,D,E,F,H,I,J,K) ----
        for (int f = 0; f < Fm; f++) {
            if (cfg.enabled[A]) expType[A] += cfg.score[A] * probAllDark(radix, p, f, cfg);
            if (cfg.enabled[B]) expType[B] += cfg.score[B] * probAllBright(radix, p, f, cfg);
            if (cfg.enabled[C]) expType[C] += cfg.score[C] * probSymmetryAllPairs(radix, p, f, cfg);
            if (cfg.enabled[D]) expType[D] += cfg.score[D] * probStripesAll(radix, p, f, cfg);
            if (cfg.enabled[E]) expType[E] += cfg.score[E] * probSideBias(radix, p, f, cfg);
            if (cfg.enabled[F]) expType[F] += cfg.score[F] * probEqualGapsBrightSet(radix, p, f, cfg);
            if (cfg.enabled[H]) expType[H] += cfg.score[H] * probMeanExtreme(radix, p, f, cfg);
            if (cfg.enabled[I]) expType[I] += cfg.score[I] * probStripeCorr(radix, p, f, cfg);
            if (cfg.enabled[J]) expType[J] += cfg.score[J] * probSymDiffSmall(radix, p, f, cfg);
            if (cfg.enabled[K]) expType[K] += cfg.score[K] * probVarHigh(radix, p, f, cfg);
        }

        // ---- per-transition contributions (G,M,N,O) ----
        for (int f = 0; f < Fm - 1; f++) {
            if (cfg.enabled[G]) expType[G] += cfg.score[G] * probChangedCountAtLeast(radix, p, f, f+1, cfg.bigChangeTh);
            if (cfg.enabled[M]) expType[M] += cfg.score[M] * probMagPerDotAtLeast(radix, p, f, f+1, cfg.magPerDotTh);
            if (cfg.enabled[N]) expType[N] += cfg.score[N] * probMaxDeltaAtLeast(radix, p, f, f+1, cfg.maxDeltaTh);
            if (cfg.enabled[O]) expType[O] += cfg.score[O] * probLeftRightDiffAtLeast(radix, p, f, f+1, cfg.lrDiffTh);
        }

        // ---- L (mirror) : once per whole sequence ----
        if (cfg.enabled[L] && cfg.enableMirrorBonus) {
            expType[L] += cfg.score[L] * probMirrorRatioAtLeast(radix, p, cfg);
        }

        // ---- P (maxChangedOverall) : once per whole sequence ----
        if (cfg.enabled[P]) {
            expType[P] += cfg.score[P] * probMaxChangedOverallAtLeast_StateDP(radix, p, cfg.maxChangedTh);
        }

        // total with lambda: frame types add directly; transition+L+P are usually in transition_score then *lambda
        // 這裡我把「哪些是frame、哪些是transition」照你的設計做：G/M/N/O/L/P 乘 lambda
        double total = 0;
        for (int t = 0; t < TYPE_COUNT; t++) {
            boolean isTransitionish = (t==G || t==M || t==N || t==O || t==L || t==P);
            total += expType[t] * (isTransitionish ? cfg.lambda : 1.0);
        }

        // 同步把 expType 也做 lambda（方便你看各type貢獻）
        double[] expTypeFinal = expType.clone();
        for (int t = 0; t < TYPE_COUNT; t++) {
            boolean isTransitionish = (t==G || t==M || t==N || t==O || t==L || t==P);
            if (isTransitionish) expTypeFinal[t] *= cfg.lambda;
        }

        return new Result(total, expTypeFinal);
    }

    // ------------------ probability building blocks ------------------

    private static double[][][] normalize(double[][][] weights, int[] radix) {
        int F = weights.length;
        int M = radix.length;
        double[][][] p = new double[F][M][];
        for (int f = 0; f < F; f++) {
            if (weights[f].length != M) throw new IllegalArgumentException("weights[f].length != radix.length");
            for (int i = 0; i < M; i++) {
                int R = radix[i];
                if (weights[f][i].length != R) throw new IllegalArgumentException("weights[f][i].length != radix[i]");
                double sum = 0;
                for (double x : weights[f][i]) sum += x;
                if (sum <= 0) throw new IllegalArgumentException("sum weight <= 0 at f=" + f + ", i=" + i);
                p[f][i] = new double[R];
                for (int v = 0; v < R; v++) p[f][i][v] = weights[f][i][v] / sum;
            }
        }
        return p;
    }

    private static double u(int v, int base) {
        int max = Math.max(1, base - 1);
        int vv = Math.max(0, Math.min(v, max));
        return vv / (double) max;
    }

    // A: all dark
    private static double probAllDark(int[] radix, double[][][] p, int f, Config cfg) {
        double ans = 1.0;
        for (int i = 0; i < radix.length; i++) {
            double pi = 0;
            for (int v = 0; v < radix[i]; v++) {
                if (u(v, radix[i]) <= cfg.epsSame) pi += p[f][i][v];
            }
            ans *= pi;
        }
        return ans;
    }

    // B: all bright (>= 1-epsSame)
    private static double probAllBright(int[] radix, double[][][] p, int f, Config cfg) {
        double ans = 1.0;
        for (int i = 0; i < radix.length; i++) {
            double pi = 0;
            for (int v = 0; v < radix[i]; v++) {
                if (u(v, radix[i]) >= 1.0 - cfg.epsSame) pi += p[f][i][v];
            }
            ans *= pi;
        }
        return ans;
    }

    // C: all symmetry pairs match within epsSame (pairs are independent)
    private static double probSymmetryAllPairs(int[] radix, double[][][] p, int f, Config cfg) {
        int M = radix.length;
        int pairs = M / 2;
        double ans = 1.0;
        for (int a = 0; a < pairs; a++) {
            int b = M - 1 - a;
            ans *= probPairMatch(radix, p[f][a], p[f][b], a, b, cfg.epsSame);
        }
        return ans;
    }

    // helper: P(|u(X)-u(Y)|<=eps)
    private static double probPairMatch(int[] radix, double[] pa, double[] pb, int ia, int ib, double eps) {
        double s = 0;
        for (int va = 0; va < radix[ia]; va++) {
            double ua = u(va, radix[ia]);
            double pva = pa[va];
            if (pva == 0) continue;
            for (int vb = 0; vb < radix[ib]; vb++) {
                double ub = u(vb, radix[ib]);
                if (Math.abs(ua - ub) <= eps) s += pva * pb[vb];
            }
        }
        return s;
    }

    // D: stripes i vs i+2 all match (splits into even chain and odd chain, independent)
    private static double probStripesAll(int[] radix, double[][][] p, int f, Config cfg) {
        int M = radix.length;
        double pe = probChainAllAdjacentMatch(radix, p, f, cfg.epsSame, 0); // even positions 0,2,4...
        double po = probChainAllAdjacentMatch(radix, p, f, cfg.epsSame, 1); // odd positions 1,3,5...
        return pe * po;
    }

    // chain DP on positions start,start+2,...
    private static double probChainAllAdjacentMatch(int[] radix, double[][][] p, int f, double eps, int start) {
        int M = radix.length;
        List<Integer> idx = new ArrayList<>();
        for (int i = start; i < M; i += 2) idx.add(i);
        if (idx.size() <= 1) return 1.0;

        // dp over previous value states (exact on discrete values)
        int first = idx.get(0);
        double[] dp = new double[radix[first]];
        for (int v = 0; v < radix[first]; v++) dp[v] = p[f][first][v];

        for (int t = 1; t < idx.size(); t++) {
            int cur = idx.get(t);
            double[] ndp = new double[radix[cur]];
            for (int w = 0; w < radix[cur]; w++) {
                double uw = u(w, radix[cur]);
                double pw = p[f][cur][w];
                if (pw == 0) continue;
                double sum = 0;
                for (int v = 0; v < dp.length; v++) {
                    if (dp[v] == 0) continue;
                    double uv = u(v, radix[idx.get(t-1)]);
                    if (Math.abs(uv - uw) <= eps) sum += dp[v];
                }
                ndp[w] = pw * sum;
            }
            dp = ndp;
        }

        double ans = 0;
        for (double x : dp) ans += x;
        return ans;
    }

    // E: side bias >= sideBiasTh (exact via convolution on sums of u)
    private static double probSideBias(int[] radix, double[][][] p, int f, Config cfg) {
        int M = radix.length;
        int half = M / 2;

        Map<Integer, Double> left = sumDist(radix, p, f, 0, half-1, 10000);  // scale=10000
        Map<Integer, Double> right = sumDist(radix, p, f, half+1, M-1, 10000);

        int th = (int)Math.round(cfg.sideBiasTh * 10000);

        // compute P(|L-R|>=th) using sorted right CDF
        int nR = right.size();
        int[] rKeys = new int[nR];
        double[] rProb = new double[nR];
        int idx = 0;
        for (var e : right.entrySet()) { rKeys[idx]=e.getKey(); rProb[idx]=e.getValue(); idx++; }
        sortByKey(rKeys, rProb);
        double[] rPrefix = prefix(rProb);

        double ans = 0;
        for (var eL : left.entrySet()) {
            int sL = eL.getKey();
            double pL = eL.getValue();
            // want R <= sL - th  OR  R >= sL + th
            int loKey = sL - th;
            int hiKey = sL + th;
            double pLE = cdfLE(rKeys, rPrefix, loKey);
            double pGE = 1.0 - cdfLT(rKeys, rPrefix, hiKey);
            ans += pL * (pLE + pGE);
        }
        return ans;
    }

    // F: bright set forms arithmetic progression (len>=3), exact (no overlap for len>=3)
    private static double probEqualGapsBrightSet(int[] radix, double[][][] p, int f, Config cfg) {
        int M = radix.length;
        double[] pb = new double[M]; // P(cell is brightDot)
        for (int i = 0; i < M; i++) {
            double s = 0;
            for (int v = 0; v < radix[i]; v++) if (u(v, radix[i]) >= cfg.brightTh) s += p[f][i][v];
            pb[i] = s;
        }

        // base: all non-bright
        double baseOff = 1.0;
        for (int i = 0; i < M; i++) baseOff *= (1.0 - pb[i]);

        // ratio r_i = pb[i]/(1-pb[i]) ; handle pb=1 separately
        double[] r = new double[M];
        boolean anyOne = false;
        for (int i = 0; i < M; i++) {
            if (pb[i] >= 1.0) { anyOne = true; r[i] = Double.POSITIVE_INFINITY; }
            else r[i] = pb[i] / (1.0 - pb[i]);
        }
        if (anyOne) {
            // 若有 pb=1，則 baseOff=0；此時只能直接枚舉progression並算 exact product
            double ans = 0;
            for (int start = 0; start < M; start++) {
                for (int step = 1; start + 2*step < M; step++) {
                    for (int len = 3; start + (len-1)*step < M; len++) {
                        boolean[] in = new boolean[M];
                        for (int t = 0; t < len; t++) in[start + t*step] = true;
                        double prob = 1.0;
                        for (int i = 0; i < M; i++) prob *= in[i] ? pb[i] : (1.0 - pb[i]);
                        ans += prob;
                    }
                }
            }
            return ans;
        }

        double ans = 0;
        // sum over all arithmetic progressions of bright indices, len>=3
        for (int start = 0; start < M; start++) {
            for (int step = 1; start + 2*step < M; step++) {
                for (int len = 3; start + (len-1)*step < M; len++) {
                    double mult = 1.0;
                    for (int t = 0; t < len; t++) mult *= r[start + t*step];
                    ans += baseOff * mult;
                }
            }
        }
        return ans;
    }

    // H: mean extreme (sum <= low*M or >= high*M), exact via sum distribution
    private static double probMeanExtreme(int[] radix, double[][][] p, int f, Config cfg) {
        int M = radix.length;
        int scale = 10000;
        Map<Integer, Double> dist = sumDist(radix, p, f, 0, M-1, scale);

        int lo = (int)Math.floor(cfg.meanULowTh * M * scale + 1e-12);
        int hi = (int)Math.ceil(cfg.meanUHighTh * M * scale - 1e-12);

        double ans = 0;
        for (var e : dist.entrySet()) {
            int s = e.getKey();
            if (s <= lo || s >= hi) ans += e.getValue();
        }
        return ans;
    }

    // I: stripeCorr = |alt|/sum >= th, exact via 2D DP over (sum, alt)
    private static double probStripeCorr(int[] radix, double[][][] p, int f, Config cfg) {
        int M = radix.length;
        int scale = 10000;

        Map<Long, Double> dp = new HashMap<>();
        dp.put(pack2(0, 0), 1.0);

        for (int i = 0; i < M; i++) {
            Map<Long, Double> ndp = new HashMap<>();
            int sign = (i % 2 == 0) ? 1 : -1;
            for (var st : dp.entrySet()) {
                int sum = unpackA(st.getKey());
                int alt = unpackB(st.getKey());
                double ps = st.getValue();
                if (ps == 0) continue;
                for (int v = 0; v < radix[i]; v++) {
                    double pv = p[f][i][v];
                    if (pv == 0) continue;
                    int uInt = (int)Math.round(u(v, radix[i]) * scale);
                    int nSum = sum + uInt;
                    int nAlt = alt + sign * uInt;
                    long key = pack2(nSum, nAlt);
                    ndp.merge(key, ps * pv, Double::sum);
                }
            }
            dp = ndp;
        }

        double ans = 0;
        for (var st : dp.entrySet()) {
            int sum = unpackA(st.getKey());
            int alt = unpackB(st.getKey());
            if (sum == 0) continue; // corr=0
            if (Math.abs((double)alt) >= cfg.stripeCorrTh * sum) ans += st.getValue();
        }
        return ans;
    }

    // J: average symmetry diff <= th, exact via pair diff convolution
    private static double probSymDiffSmall(int[] radix, double[][][] p, int f, Config cfg) {
        int M = radix.length;
        int pairs = M / 2;
        if (pairs == 0) return 0;

        int scale = 10000;
        Map<Integer, Double> dist = new HashMap<>();
        dist.put(0, 1.0);

        for (int a = 0; a < pairs; a++) {
            int b = M - 1 - a;
            Map<Integer, Double> one = pairAbsDiffDist(radix, p, f, a, b, scale);
            dist = convolve(dist, one);
        }

        int th = (int)Math.floor(cfg.symDiffAvgTh * pairs * scale + 1e-12);
        double ans = 0;
        for (var e : dist.entrySet()) if (e.getKey() <= th) ans += e.getValue();
        return ans;
    }

    // K: variance >= th, exact via 2D DP over (sumU, sumU2)
    private static double probVarHigh(int[] radix, double[][][] p, int f, Config cfg) {
        int M = radix.length;
        int scale = 10000;

        Map<Long, Double> dp = new HashMap<>();
        dp.put(packSQ(0, 0L), 1.0);

        for (int i = 0; i < M; i++) {
            Map<Long, Double> ndp = new HashMap<>();
            for (var st : dp.entrySet()) {
                int sum = unpackS(st.getKey());
                long q = unpackQ(st.getKey());
                double ps = st.getValue();
                if (ps == 0) continue;

                for (int v = 0; v < radix[i]; v++) {
                    double pv = p[f][i][v];
                    if (pv == 0) continue;
                    int uInt = (int)Math.round(u(v, radix[i]) * scale);
                    int nSum = sum + uInt;
                    long nQ = q + 1L*uInt*uInt;
                    long key = packSQ(nSum, nQ);
                    ndp.merge(key, ps * pv, Double::sum);
                }
            }
            dp = ndp;
        }

        // inequality: var >= th
        // Using scaled uInt = u*scale:
        // var ≈ (Q/M - (S/M)^2) / scale^2  >= varHighTh
        // <=> M*Q - S^2 >= varHighTh * scale^2 * M^2
        double rhs = cfg.varHighTh * (double)scale * (double)scale * (double)M * (double)M;

        double ans = 0;
        for (var st : dp.entrySet()) {
            int S = unpackS(st.getKey());
            long Q = unpackQ(st.getKey());
            double lhs = (double)M * (double)Q - (double)S * (double)S;
            if (lhs >= rhs) ans += st.getValue();
        }
        return ans;
    }

    // ---- transitions ----

    // G: P(changedCount >= k) where changed at lamp i is (X!=Y)
    private static double probChangedCountAtLeast(int[] radix, double[][][] p, int f1, int f2, int k) {
        int M = radix.length;
        double[] pc = new double[M];
        for (int i = 0; i < M; i++) {
            double same = 0;
            for (int v = 0; v < radix[i]; v++) same += p[f1][i][v] * p[f2][i][v];
            pc[i] = 1.0 - same;
        }
        return poissonBinomialTail(pc, k);
    }

    // M: magPerDot = (Σ |Δu|)/M >= th
    private static double probMagPerDotAtLeast(int[] radix, double[][][] p, int f1, int f2, double th) {
        int M = radix.length;
        int scale = 10000;
        int need = (int)Math.ceil(th * M * scale - 1e-12);

        Map<Integer, Double> dist = new HashMap<>();
        dist.put(0, 1.0);
        for (int i = 0; i < M; i++) {
            Map<Integer, Double> one = absDiffDist(radix, p, f1, f2, i, scale);
            dist = convolve(dist, one);
        }

        double ans = 0;
        for (var e : dist.entrySet()) if (e.getKey() >= need) ans += e.getValue();
        return ans;
    }

    // N: maxDelta = max_i |Δu| >= th
    private static double probMaxDeltaAtLeast(int[] radix, double[][][] p, int f1, int f2, double th) {
        int M = radix.length;
        int scale = 10000;
        int t = (int)Math.ceil(th * scale - 1e-12);

        double pAllLT = 1.0;
        for (int i = 0; i < M; i++) {
            double piLT = 0;
            for (int a = 0; a < radix[i]; a++) {
                double pa = p[f1][i][a];
                if (pa == 0) continue;
                int ua = (int)Math.round(u(a, radix[i]) * scale);
                for (int b = 0; b < radix[i]; b++) {
                    double pb = p[f2][i][b];
                    if (pb == 0) continue;
                    int ub = (int)Math.round(u(b, radix[i]) * scale);
                    if (Math.abs(ua - ub) < t) piLT += pa * pb;
                }
            }
            pAllLT *= piLT;
        }
        return 1.0 - pAllLT;
    }

    // O: |leftChanged - rightChanged| >= th (center ignored if odd)
    private static double probLeftRightDiffAtLeast(int[] radix, double[][][] p, int f1, int f2, int th) {
        int M = radix.length;
        int half = M/2;

        List<Double> pL = new ArrayList<>();
        List<Double> pR = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            if (i == half && (M % 2 == 1)) continue;
            double same = 0;
            for (int v = 0; v < radix[i]; v++) same += p[f1][i][v] * p[f2][i][v];
            double pc = 1.0 - same;
            if (i < half) pL.add(pc);
            else pR.add(pc);
        }

        double[] dl = poissonBinomialPMF(pL);
        double[] dr = poissonBinomialPMF(pR);

        double ans = 0;
        for (int a = 0; a < dl.length; a++) {
            for (int b = 0; b < dr.length; b++) {
                if (Math.abs(a - b) >= th) ans += dl[a] * dr[b];
            }
        }
        return ans;
    }

    // ---- L: mirror ratio >= mirrorRatioTh over paired frames (0<->F-1,1<->F-2,...)
    private static double probMirrorRatioAtLeast(int[] radix, double[][][] p, Config cfg) {
        int F = p.length;
        int M = radix.length;
        int pairs = F / 2;
        int selfM = (F % 2 == 1) ? M : 0;

        // undirected comparisons count = pairs * M
        int E = pairs * M;
        if (E == 0) return 1.0; // 沒有配對時，ratio=1（只有self或空），按你需求可改

        // Each match contributes 2 in your loop; condition converts to X>=need
        // ratio = (2X + selfM) / (2E + selfM) >= r
        // 2X >= r(2E+selfM) - selfM
        double rhs = cfg.mirrorRatioTh * (2.0*E + selfM) - selfM;
        int need = (int)Math.ceil(rhs / 2.0 - 1e-12);
        if (need <= 0) return 1.0;
        if (need > E) return 0.0;

        double[] pm = new double[E]; // match prob for each edge
        int idx = 0;
        for (int f = 0; f < pairs; f++) {
            int g = F - 1 - f;
            for (int i = 0; i < M; i++) {
                pm[idx++] = probPairMatch(radix, p[f][i], p[g][i], i, i, cfg.epsSame);
            }
        }

        return poissonBinomialTail(pm, need);
    }

    // ---- P: maxChangedOverall >= th (global over transitions), exact via state DP over whole-frame states
    // This is exact but complexity depends on S = Π radix[i]. Works well when lamps are binary/ternary and M small.
    private static double probMaxChangedOverallAtLeast_StateDP(int[] radix, double[][][] p, int th) {
        int F = p.length;
        int M = radix.length;

        int S = 1;
        for (int r : radix) {
            if (S > 1_000_000 / Math.max(1, r)) {
                throw new IllegalStateException("State space too large for exact P without sampling. "
                        + "S=prod(radix) is huge. Consider disabling P or reducing radix/M.");
            }
            S *= r;
        }

        // enumerate all states as mixed radix
        int[][] stateVals = new int[S][M];
        for (int s = 0; s < S; s++) {
            int x = s;
            for (int i = 0; i < M; i++) {
                stateVals[s][i] = x % radix[i];
                x /= radix[i];
            }
        }

        // frame state probabilities P_f(s) = Π_i p[f][i][stateVals[s][i]]
        double[][] Pf = new double[F][S];
        for (int f = 0; f < F; f++) {
            for (int s = 0; s < S; s++) {
                double pr = 1.0;
                for (int i = 0; i < M; i++) pr *= p[f][i][stateVals[s][i]];
                Pf[f][s] = pr;
            }
        }

        // compatibility: ok(s,t) iff changedCount(s,t) < th
        boolean[][] ok = new boolean[S][S];
        for (int s = 0; s < S; s++) {
            for (int t = 0; t < S; t++) {
                int changed = 0;
                for (int i = 0; i < M; i++) if (stateVals[s][i] != stateVals[t][i]) changed++;
                ok[s][t] = (changed < th);
            }
        }

        // DP: probability all transitions are OK (maxChanged < th)
        double[] alpha = new double[S];
        System.arraycopy(Pf[0], 0, alpha, 0, S);

        for (int f = 0; f < F - 1; f++) {
            double[] next = new double[S];
            for (int t = 0; t < S; t++) {
                double sum = 0;
                for (int s = 0; s < S; s++) {
                    if (!ok[s][t]) continue;
                    sum += alpha[s];
                }
                next[t] = Pf[f+1][t] * sum;
            }
            alpha = next;
        }

        double pAllOk = 0;
        for (double x : alpha) pAllOk += x;

        return 1.0 - pAllOk; // P(maxChanged >= th)
    }

    // ------------------ DP helpers ------------------

    // distribution of sum of u over i in [l..r], using integer scale
    private static Map<Integer, Double> sumDist(int[] radix, double[][][] p, int f, int l, int r, int scale) {
        if (l > r) {
            Map<Integer, Double> d = new HashMap<>();
            d.put(0, 1.0);
            return d;
        }
        Map<Integer, Double> dist = new HashMap<>();
        dist.put(0, 1.0);

        for (int i = l; i <= r; i++) {
            Map<Integer, Double> one = new HashMap<>();
            for (int v = 0; v < radix[i]; v++) {
                double pv = p[f][i][v];
                if (pv == 0) continue;
                int uInt = (int)Math.round(u(v, radix[i]) * scale);
                one.merge(uInt, pv, Double::sum);
            }
            dist = convolve(dist, one);
        }
        return dist;
    }

    private static Map<Integer, Double> pairAbsDiffDist(int[] radix, double[][][] p, int f, int ia, int ib, int scale) {
        Map<Integer, Double> dist = new HashMap<>();
        for (int va = 0; va < radix[ia]; va++) {
            double pa = p[f][ia][va];
            if (pa == 0) continue;
            int ua = (int)Math.round(u(va, radix[ia]) * scale);
            for (int vb = 0; vb < radix[ib]; vb++) {
                double pb = p[f][ib][vb];
                if (pb == 0) continue;
                int ub = (int)Math.round(u(vb, radix[ib]) * scale);
                dist.merge(Math.abs(ua - ub), pa * pb, Double::sum);
            }
        }
        return dist;
    }

    private static Map<Integer, Double> absDiffDist(int[] radix, double[][][] p, int f1, int f2, int i, int scale) {
        Map<Integer, Double> dist = new HashMap<>();
        for (int a = 0; a < radix[i]; a++) {
            double pa = p[f1][i][a];
            if (pa == 0) continue;
            int ua = (int)Math.round(u(a, radix[i]) * scale);
            for (int b = 0; b < radix[i]; b++) {
                double pb = p[f2][i][b];
                if (pb == 0) continue;
                int ub = (int)Math.round(u(b, radix[i]) * scale);
                dist.merge(Math.abs(ua - ub), pa * pb, Double::sum);
            }
        }
        return dist;
    }

    private static Map<Integer, Double> convolve(Map<Integer, Double> a, Map<Integer, Double> b) {
        Map<Integer, Double> out = new HashMap<>();
        for (var ea : a.entrySet()) {
            int sa = ea.getKey();
            double pa = ea.getValue();
            if (pa == 0) continue;
            for (var eb : b.entrySet()) {
                int sb = eb.getKey();
                double pb = eb.getValue();
                if (pb == 0) continue;
                out.merge(sa + sb, pa * pb, Double::sum);
            }
        }
        return out;
    }

    private static double poissonBinomialTail(double[] p, int k) {
        // dp[c] = P(exactly c)
        double[] dp = new double[p.length + 1];
        dp[0] = 1.0;
        for (double pi : p) {
            double[] ndp = new double[dp.length];
            for (int c = 0; c < dp.length; c++) {
                if (dp[c] == 0) continue;
                ndp[c] += dp[c] * (1.0 - pi);
                if (c + 1 < dp.length) ndp[c + 1] += dp[c] * pi;
            }
            dp = ndp;
        }
        double ans = 0;
        for (int c = k; c < dp.length; c++) ans += dp[c];
        return ans;
    }

    private static double[] poissonBinomialPMF(List<Double> ps) {
        double[] dp = new double[ps.size() + 1];
        dp[0] = 1.0;
        for (double pi : ps) {
            double[] ndp = new double[dp.length];
            for (int c = 0; c < dp.length; c++) {
                if (dp[c] == 0) continue;
                ndp[c] += dp[c] * (1.0 - pi);
                if (c + 1 < dp.length) ndp[c + 1] += dp[c] * pi;
            }
            dp = ndp;
        }
        return dp;
    }

    // sorting helpers
    private static void sortByKey(int[] keys, double[] vals) {
        Integer[] idx = new Integer[keys.length];
        for (int i = 0; i < idx.length; i++) idx[i] = i;
        Arrays.sort(idx, Comparator.comparingInt(i -> keys[i]));
        int[] nk = new int[keys.length];
        double[] nv = new double[vals.length];
        for (int i = 0; i < idx.length; i++) { nk[i] = keys[idx[i]]; nv[i] = vals[idx[i]]; }
        System.arraycopy(nk, 0, keys, 0, keys.length);
        System.arraycopy(nv, 0, vals, 0, vals.length);
    }

    private static double[] prefix(double[] p) {
        double[] pre = new double[p.length+1];
        for (int i = 0; i < p.length; i++) pre[i+1] = pre[i] + p[i];
        return pre;
    }

    private static double cdfLE(int[] keys, double[] pre, int x) {
        int idx = upperBound(keys, x);
        return pre[idx];
    }
    private static double cdfLT(int[] keys, double[] pre, int x) {
        int idx = lowerBound(keys, x);
        return pre[idx];
    }
    private static int lowerBound(int[] a, int x) {
        int l=0,r=a.length;
        while(l<r){int m=(l+r)>>>1; if(a[m] < x) l=m+1; else r=m;}
        return l;
    }
    private static int upperBound(int[] a, int x) {
        int l=0,r=a.length;
        while(l<r){int m=(l+r)>>>1; if(a[m] <= x) l=m+1; else r=m;}
        return l;
    }

    // pack/unpack for (sum, alt) ints
    private static long pack2(int a, int b) { return (((long)a) << 32) ^ (b & 0xffffffffL); }
    private static int unpackA(long k) { return (int)(k >> 32); }
    private static int unpackB(long k) { return (int)k; }

    // pack/unpack for (S, Q) where Q is long
    private static long packSQ(int s, long q) {
        // store s in high 20 bits? easier: use two-level: (s<<44) + (q & ((1<<44)-1)) is risky
        // safest: hash into long via pair hashing; we only use as map key, not reversible globally.
        // But we need reversible -> use two longs packed in one long is impossible.
        // Here we do reversible by storing q in lower 32 bits is not enough sometimes.
        // So we use a bijection based on String-like? too heavy.
        // Practical reversible approach: store key as ( ((long)s) << 48 ) | (q & 0x0000FFFFFFFFFFFFL )
        // assuming q fits in 48 bits. With scale=10000 and M<=32, q <= 32*(1e8)=3.2e9 fits < 2^32, safe.
        if (q < 0 || q > 0x0000FFFFFFFFFFFFL) throw new IllegalStateException("Q too large for packing: " + q);
        return (((long)s) << 48) | (q & 0x0000FFFFFFFFFFFFL);
    }
    private static int unpackS(long key) { return (int)(key >> 48); }
    private static long unpackQ(long key) { return key & 0x0000FFFFFFFFFFFFL; }
}
