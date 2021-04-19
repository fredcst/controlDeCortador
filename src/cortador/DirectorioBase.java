package cortador;

import java.io.File;

public class DirectorioBase {

	public DirectorioBase() {}
	

	public static File getFileFromResources(String fileName) {
		File file = null;
		ClassLoader clL = Thread.currentThread().getContextClassLoader();
		java.net.URL resource = clL.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			file = new File(resource.getFile());
		}
		return file;
	}
	public static String getNombreFileFromResources(String fileName) {
		String fileNom = null;
		ClassLoader clL = Thread.currentThread().getContextClassLoader();
		java.net.URL resource = clL.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			fileNom = resource.getFile();
		}
		return fileNom;
	}
}
