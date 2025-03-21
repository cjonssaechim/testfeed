package me.saechimdaeki.testfeed.common.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileStorageService {

	@Value("${image.url}")
	private String imagePath;

	@Value("${host}")
	private String host;

	public String saveFile(MultipartFile file) throws IOException {

		final Path uploadDir = Paths.get(imagePath).toAbsolutePath().normalize();

		if (file.isEmpty()) {
			throw new IOException("빈 파일입니다.");
		}

		Files.createDirectories(uploadDir);

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path targetPath = uploadDir.resolve(fileName);

		Files.write(targetPath, file.getBytes());

		fileName = host+ "/" + imagePath + "/" + fileName;

		return fileName;
	}

	public List<String> saveFiles(List<MultipartFile> files) throws IOException {
		List<String> fileUrls = new ArrayList<>();
		for (MultipartFile file : files) {
			if (file != null && !file.isEmpty()) {
				fileUrls.add(saveFile(file));
			}
		}
		return fileUrls;
	}
}
