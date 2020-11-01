import java.io.File

fun main(args: Array<String>) {

    val dataPath = "C:/DeepFake SCRIPT"
    val idDic =  mutableMapOf<String, String>()

    File("$dataPath/id_list.txt").forEachLine() {
        var cleanLine = it
        if (it.startsWith("\uFEFF")) {
            cleanLine = it.substring(1);
        }
        if (cleanLine.contains("=")) {
            val splitIt = cleanLine.replace(" ", "").split("=")
            idDic[splitIt[0]] = splitIt[1]
        }
    }

    //Directories
    File(dataPath).walk().forEach {
        if (it.isDirectory && it.name.startsWith("id")) {
            val splitOGFileName = it.name.split("_")
            val nameReal = idDic[splitOGFileName[0]]
            val nameDF = idDic[splitOGFileName[1]]
            val newFileName = "%s_%s_%s_%s".format(splitOGFileName[0], splitOGFileName[1], nameReal, nameDF)
            val newFile = File(it.parent + "/" + newFileName)
            it.renameTo(newFile)
        }
    }

    //Files
    File(dataPath).walk().forEach {
        if (it.isFile && it.name.startsWith("id") && it.name.endsWith(".mp4")) {
            var newFileName = ""
            val splitOGFileName = it.name.split("_")
            if (splitOGFileName.size == 2) {
                val nameReal = idDic[splitOGFileName[0]]
                newFileName = "%s_%s_%s".format(splitOGFileName[0], nameReal, splitOGFileName[1])
            }
            else if(splitOGFileName.size == 3) {
                val nameReal = idDic[splitOGFileName[0]]
                val nameDF = idDic[splitOGFileName[1]]
                newFileName = "%s_%s_%s_%s_%s".format(splitOGFileName[0], splitOGFileName[1], nameReal, nameDF, splitOGFileName[2])
            }
            else{
                return@forEach
            }

            val newFile = File(it.parent + "/" + newFileName)
            it.renameTo(newFile)
        }
    }
}