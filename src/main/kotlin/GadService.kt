internal class GadService(val driveAdapter: DriveAdapter) {

  fun createForms() {
    val files = driveAdapter.listFiles()
    for (file in files) {
      System.out.printf("%s (%s)\n", file.getName(), file.getId())
    }

    driveAdapter.copyFile("1cFehOGviFNL9MucRBDx_K8E-UEv9309_HFBS-xix84M", "piwo");
  }
}