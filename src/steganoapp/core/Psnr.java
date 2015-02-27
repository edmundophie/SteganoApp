public class Psnr {

  public static double log10(double x) {
    return Math.log(x)/Math.log(10);
  }

  public static void main (String[] args) {
    int     nrows, ncols;
    int     img1[][], img2[][];
    double  peak, signal, noise, mse;

    if (args.length != 4) {
      System.out.println("Usage: Psnr <nrows> <ncols> <img1> <img2>");
      return;
    }
    nrows = Integer.parseInt(args[0]);
    ncols = Integer.parseInt(args[1]);
    img1 = new int[nrows][ncols];
    img2 = new int[nrows][ncols];
    ArrayIO.readByteArray(args[2], img1, nrows, ncols);
    ArrayIO.readByteArray(args[3], img2, nrows, ncols);

    signal = noise = peak = 0;
    for (int i=0; i<nrows; i++) {
      for (int j=0; j<ncols; j++) {
        signal += img1[i][j] * img1[i][j];
        noise += (img1[i][j] - img2[i][j]) * (img1[i][j] - img2[i][j]);
        if (peak < img1[i][j])
          peak = img1[i][j];
      }
    }

    mse = noise/(nrows*ncols); // Mean square error
    System.out.println("MSE: " + mse);
    System.out.println("SNR: " + 10*log10(signal/noise));
    System.out.println("PSNR(max=255): " + (10*log10(255*255/mse)));
    System.out.println("PSNR(max=" + peak + "): " + 10*log10((peak*peak)/mse));
  }
}