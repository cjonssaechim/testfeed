package me.saechimdaeki.testfeed.common.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileStorageService {
	private final Path uploadDir;

	public FileStorageService(@Value("${image.url}") String uploadDir) {
		this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
	}

	public String saveFile(MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			throw new IOException("빈 파일입니다.");
		}

		Files.createDirectories(this.uploadDir);

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path targetPath = this.uploadDir.resolve(fileName);

		Files.write(targetPath, file.getBytes());

		fileName = uploadDir + "/" + fileName;

		return fileName;
	}
}
