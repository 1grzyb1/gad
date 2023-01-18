data class Team(val hour: String, val code: String, val teamName: String) {
  fun getFileName(): String {
    return "$`code`_$teamName"
  }

  fun getAge(): String {
    return code[3].toString()
  }
}
