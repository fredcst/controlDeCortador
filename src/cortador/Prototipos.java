package cortador;

import java.io.File;
import java.util.LinkedList;

public class Prototipos {
	int noPrototipos = 0;
	LinkedList<CurvasDeBezierDeObjeto> curvasObjetoList = null;

	public int getNoPrototipos() {
		return noPrototipos;
	}

	public void setNoPrototipos(int noPrototipos) {
		this.noPrototipos = noPrototipos;
	}

	public LinkedList<CurvasDeBezierDeObjeto> getCurvasObjetoList() {
		return curvasObjetoList;
	}

	public void setCurvasObjetoList(LinkedList<CurvasDeBezierDeObjeto> curvasObjetoList) {
		this.curvasObjetoList = curvasObjetoList;
	}
/**
 * Lee todos los prototipos desde directorio: cortador/objetos/normal_svg
 */
	public void leePrototiposDesdeFile_Bezier_Normal() {
	
		String directorio_normal = null;

		File directorio_normalD = null;
		directorio_normal = DirectorioBase
				.getNombreFileFromResources("cortador" + File.separator + "objetos" + File.separator + "normal_svg");
		directorio_normalD = new File(directorio_normal);
		noPrototipos = directorio_normalD.listFiles().length;
		System.out.format("No total de Prototipos:%d\n", noPrototipos);
		curvasObjetoList = new LinkedList<CurvasDeBezierDeObjeto>();
		CurvasDeBezierDeObjeto proto = null;
		if (curvasObjetoList != null) {
			for (final File fileEntry : directorio_normalD.listFiles()) {
				if (fileEntry.isDirectory()) {
					// listFilesForFolder(fileEntry);
				} else {
					String nombre_svgP = fileEntry.getName();
					proto = new CurvasDeBezierDeObjeto();
					// noPiezas = scanner.nextInt();
					proto.obtiene_curvas_de_bezier_normal(nombre_svgP);
					curvasObjetoList.add(proto);
				}

			}
		}

	}

}
