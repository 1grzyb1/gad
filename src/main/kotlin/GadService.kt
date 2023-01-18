import com.google.api.services.drive.model.File
import com.google.api.services.sheets.v4.model.Sheet

internal class GadService(
  private val driveAdapter: DriveAdapter,
  private val sheetsAdapter: SpreadSheetsAdapter
) {

  private val templates = getTemplates()

  fun createForms() {
    val sheets = sheetsAdapter.getSheets()
    for (sheet in sheets!!) {
      processSheet(sheet)
    }
  }

  private fun processSheet(sheet: Sheet) {
    val title = sheet.properties.title
    println("Processing: $title")

    val teams = sheetsAdapter.geTeams(title)
    val template = getTemplate(title)

    processTeams(teams, template)
  }

  private fun processTeams(teams: Teams, template: File) {
    for (team in teams.teams) {
      val file = driveAdapter.copyFile(template.id, team.getFileName())
      templateCell(file.id, "A1", team.getAge())
      templateCell(file.id, "A2", team.teamName)
      templateCell(file.id, "A3", teams.judges)
      println("Created: ${file.name}")
    }
  }

  fun templateCell(sheetId: String, cell: String, value: String) {
    val cellValue = sheetsAdapter.getCellValue(cell, sheetId)
    sheetsAdapter.writeCell(cell, cellValue.replace("XXX", value), sheetId)
  }

  private fun getTemplate(sheetName: String): File {
    val problem = sheetName.split(" ")[1][0]
    return templates[problem]!!
  }

  private fun getTemplates(): Map<Char, File> {
    return driveAdapter
      .listFiles()
      .filter { it.name.endsWith("_KOD_NAZWA") }
      .associateBy { it.name[1] }
  }
}