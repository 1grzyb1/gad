import com.google.api.client.json.gson.GsonFactory

internal class GadConfiguration(val fromFolderId: String, val toFolderId: String, val zspId: String) {

  fun gadService(): GadService {
    val credentials = CredentialsProvider().getCredentials()
    val jsonFactory = GsonFactory.getDefaultInstance()
    val driveAdapter = DriveAdapter(credentials, jsonFactory, fromFolderId, toFolderId)
    val sheetsAdapter = SpreadSheetsAdapter(credentials, jsonFactory, zspId)
    return GadService(driveAdapter, sheetsAdapter)
  }
}