/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.test.unit;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * @project node-server
 * @create 2013-8-14-下午12:32:51
 * @author pippo
 */
public class AddJAVAFileHeader {

	private static String copy_right = "/* Copyright © 2010 www.myctu.cn. All rights reserved. */\n";

	private static void process(String root) throws IOException {

		Collection<File> java_files = FileUtils.listFiles(new File(root), new IOFileFilter() {

			private static final String JAVA_SUFFFIX = "java";

			@Override
			public boolean accept(File dir, String name) {
				return JAVA_SUFFFIX.equals(FilenameUtils.getExtension(name).toLowerCase());
			}

			@Override
			public boolean accept(File file) {
				return JAVA_SUFFFIX.equals(FilenameUtils.getExtension(file.getName()).toLowerCase());
			}

		}, TrueFileFilter.INSTANCE);

		for (File file : java_files) {
			System.out.printf("process file:[%s]\n", file.getCanonicalPath());

			String java = FileUtils.readFileToString(file);
			if (java.startsWith(copy_right)) {
				continue;
			}

			FileUtils.writeStringToFile(file, copy_right + java);
		}

	}

	public static void main(String[] args) throws IOException {
		process("/Users/pippo/Documents/projects/platform/common");

		process("/Users/pippo/Documents/projects/notification-server/master/");
		process("/Users/pippo/Documents/projects/platform/components/service-gateway/trunk");
		process("/Users/pippo/Documents/projects/platform/components/cas/trunk");
		process("/Users/pippo/Documents/projects/platform/components/resource/trunk");
	}
}
