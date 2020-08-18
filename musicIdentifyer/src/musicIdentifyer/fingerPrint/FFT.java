package musicIdentifyer.fingerPrint;

/**
 * Created by Charles on 2016/10/29.
 */
public class FFT {

    public static final int WINDOW_SIZE = 4096;


    public static double[] fft(double[] slice) {
        if (slice.length != WINDOW_SIZE)
            throw new RuntimeException("FFT::fft(double[] slice) - " +
                    "The window size is not equal to the required window size (" + WINDOW_SIZE + ")");

        Complex[] x = new Complex[WINDOW_SIZE];

        /**
         * Convert the time-domain series as Complex series whose imaginary parts are zeros.
         */
        for (int i = 0; i < WINDOW_SIZE; ++i) {
            x[i] = new Complex(slice[i], 0);
        }

        Complex[] res = fft(x);

        double[] ret = new double[WINDOW_SIZE];
        for (int i = 0; i < WINDOW_SIZE; ++i) {
            /**
             * The magnitude of each frequency.
             */
            ret[i] = res[i].abs();
        }
        return ret;
    }

    private static Complex[] fft(Complex[] y){
        bit_reverse(y);
        int j, k, h;
        for (h = 2; h <= y.length; h <<= 1) {
            // twiddle factor 旋转因子
            Complex omega_n = new Complex(Math.cos(-2 * Math.PI / h), Math.sin(-2 * Math.PI / h));
            for (j = 0; j < y.length; j += h) {
                Complex omega = new Complex(1, 0);
                // butterfly transformation
                for (k = j; k < j + h / 2; ++k) {
                    Complex  u = y[k];
                    Complex  t = omega.mul(y[k + h / 2]);
                    y[k] = u.add(t);
                    y[k + h/2] = u.sub(t);
                    omega = omega.mul(omega_n);
                }
            }
        }
        return y;
    }

    private static void bit_reverse(Complex[] y) {
        int i, j, k;
        for (i = 1, j = y.length / 2; i < y.length - 1; ++i) {
            if (i < j) {
                Complex temp = y[i];
                y[i] = y[j];
                y[j] = temp;
            }
            k = y.length / 2;
            while (j >= k) {
                j -= k;            // eliminate leading 1.
                k /= 2;
                if (k == 0)        // cutting branches.
                    break;
            }
            if (j < k)             // add leading 1.
                j += k;
        }
    }
}