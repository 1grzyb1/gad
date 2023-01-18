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
import com.google.api.services.drive.DriveScopes
import com.google.api.services.sheets.v4.SheetsScopes.SPREADSHEETS
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader

internal class CredentialsProvider {

  private val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()

  private val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

  private val tokensFilePath = "tokens"

  private val scopes = listOf(DriveScopes.DRIVE, SPREADSHEETS)

  private val credentialFilePath = "/creds.json"

  fun getCredentials(): Credential {

    val credentialsStream =
      CredentialsProvider::class.java.getResourceAsStream(credentialFilePath)
        ?: throw FileNotFoundException("Resource not found: $credentialFilePath")
    val clientSecrets: GoogleClientSecrets = GoogleClientSecrets.load(
      jsonFactory,
      InputStreamReader(credentialsStream)
    )

    val flow: GoogleAuthorizationCodeFlow = GoogleAuthorizationCodeFlow.Builder(
      httpTransport, jsonFactory, clientSecrets, scopes
    )
      .setDataStoreFactory(
        FileDataStoreFactory(
          File(tokensFilePath)
        )
      )
      .setAccessType("offline")
      .build()
    val receiver: LocalServerReceiver = LocalServerReceiver.Builder().setPort(8888).build()

    return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
  }
}