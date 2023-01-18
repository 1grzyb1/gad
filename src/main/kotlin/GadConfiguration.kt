import com.google.api.client.json.gson.GsonFactory

internal class GadConfiguration(
  val fromFolderId: String,
  val toFolderId: String,
  val zspId: String
) {

  fun gadService(): GadService {
    val credentials = CredentialsProvider().getCredentials()
    val jsonFactory = GsonFactory.getDefaultInstance()
    val driveAdapter = DriveAdapter(credentials, jsonFactory, fromFolderId, toFolderId)
    val sheetsAdapter = SpreadSheetsAdapter(credentials, jsonFactory, zspId)

    val problemPunctuationCells =
      mapOf(Pair("1", PunctationCells("E34", "E41", "E48")),
            Pair("2", PunctationCells("E35", "E42", "E49")),
            Pair("3", PunctationCells("E35", "E52", "E45")),
            Pair("4", PunctationCells("E23", "E39", "E46")),
            Pair("5", PunctationCells("E35", "E44", "E51"))
      )
    return GadService(driveAdapter, sheetsAdapter, problemPunctuationCells)
  }
}