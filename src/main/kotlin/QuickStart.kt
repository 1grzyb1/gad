import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.FileList
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException

/* class to demonstarte use of Drive files list API */
object DriveQuickstart {
  /**
   * Application name.
   */
  private const val APPLICATION_NAME = "Google Drive API Java Quickstart"

  /**
   * Global instance of the JSON factory.
   */
  private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

  /**
   * Directory to store authorization tokens for this application.
   */
  private const val TOKENS_DIRECTORY_PATH = "tokens"

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  private val SCOPES = listOf<String>(DriveScopes.DRIVE_METADATA_READONLY, SheetsScopes.SPREADSHEETS_READONLY)
  private const val CREDENTIALS_FILE_PATH = "/creds.json"

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  @Throws(IOException::class)
  private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
    // Load client secrets.
    val `in` =
      DriveQuickstart::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
        ?: throw FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH)
    val clientSecrets: GoogleClientSecrets = GoogleClientSecrets.load(
        JSON_FACTORY,
        InputStreamReader(`in`)
      )

    // Build flow and trigger user authorization request.
    val flow: GoogleAuthorizationCodeFlow = GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
      )
        .setDataStoreFactory(
          FileDataStoreFactory(
            File(TOKENS_DIRECTORY_PATH)
          )
        )
        .setAccessType("offline")
        .build()
    val receiver: LocalServerReceiver = LocalServerReceiver.Builder().setPort(8888).build()
    //returns an authorized Credential object.
    return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
  }

  @Throws(IOException::class, GeneralSecurityException::class)
  @JvmStatic
  fun main(args: Array<String>) {
    // Build a new authorized API client service.
    val HTTP_TRANSPORT: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val credentials = getCredentials(HTTP_TRANSPORT)
    val service: Drive = Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
      .setApplicationName(APPLICATION_NAME)
      .build()

    val sheets = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
      .setApplicationName("translatointaor")
      .build()

    val values = sheets.spreadsheets().values()
      .get("1jX8WXafOFn_RGgXvRd0uclcKfi3JOlB91sMWAk3kgiA", "Arkusz1!A2:E")
      .execute()
      .getValues()


    // Print the names and IDs for up to 10 files.
    val result: FileList = service.files().list()
      .setPageSize(10)
      .setFields("nextPageToken, files(id, name)")
      .execute()
    val files = result.getFiles()
    if (files == null || files.isEmpty()) {
      println("No files found.")
    } else {
      println("Files:")
      for (file in files) {
        System.out.printf("%s (%s)\n", file.getName(), file.getId())
      }
    }
  }
}