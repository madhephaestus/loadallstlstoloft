import java.nio.file.Paths

import com.neuronrobotics.bowlerstudio.vitamins.Vitamins
import com.neuronrobotics.nrconsole.util.FileSelectionFactory

import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.FileUtil

//Your code here
File directory = FileSelectionFactory.GetDirectory(new File("."))

// Populates the array with names of files and directories
pathnames = directory.list();
def stls = []
// For each pathname in the pathnames array
for (String pathname : pathnames) {
  if(pathname.toLowerCase().endsWith(".stl")) {
	  File stlFile = new File(directory.getAbsolutePath()+File.separator+pathname)
	  stls.add(Vitamins.get(stlFile))
  }
}

def hulls=[]

for(int i=0;i<stls.size()-1;i++) {
	println "Making hull section "+(i+1)+" out of "+(stls.size()-1)
	CSG section = stls[i].union(stls[i+1]).hull()
	hulls.add(section)
}
println "Making fullCrossSection, this could take some time..."
CSG finalPart= CSG.unionAll(hulls)
String filename = directory.getAbsolutePath()+File.separator+"AllInOne.stl"
FileUtil.write(Paths.get(filename),
		finalPart.toStlString());
println "STL EXPORT to "+filename
return finalPart