import com.google.api.client.json.gson.GsonFactory

internal class GadConfiguration(val fromFolderId: String) {

  fun gadService(): GadService {
    val credentials = CredentialsProvider().getCredentials()
    val jsonFactory = GsonFactory.getDefaultInstance()
    val driveAdapter = DriveAdapter(credentials, jsonFactory, fromFolderId)
    return GadService(driveAdapter)
  }
}