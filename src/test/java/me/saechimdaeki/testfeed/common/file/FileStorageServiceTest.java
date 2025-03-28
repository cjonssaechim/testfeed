package me.saechimdaeki.testfeed.common.file;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class FileStorageServiceTest {

	private FileStorageService fileStorageService;
	private final String testUploadPath = "test-uploads";
	private final String testHost = "http://localhost:8080";

	@BeforeEach
	void setUp() throws Exception {
		fileStorageService = new FileStorageService();
		injectPrivateField(fileStorageService, "imagePath", testUploadPath);
		injectPrivateField(fileStorageService, "host", testHost);
	}

	@AfterEach
	void cleanUp() throws IOException {
		Path dir = Paths.get(testUploadPath);
		if (Files.exists(dir)) {
			Files.walk(dir)
				.map(Path::toFile)
				.sorted((a, b) -> -a.compareTo(b)) // 파일 먼저 삭제
				.forEach(File::delete);
		}
	}

	@Test
	void saveFile_success() throws IOException {
		MultipartFile mockFile = new MockMultipartFile(
			"file",
			"test.png",
			"image/png",
			"Hello World!".getBytes()
		);

		String savedUrl = fileStorageService.saveFile(mockFile);

		assertThat(savedUrl).startsWith(testHost);
		assertThat(Files.list(Paths.get(testUploadPath)).count()).isEqualTo(1);
	}

	@Test
	void saveFile_throwsException_onEmptyFile() {
		MultipartFile emptyFile = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);

		assertThatThrownBy(() -> fileStorageService.saveFile(emptyFile))
			.isInstanceOf(IOException.class)
			.hasMessage("빈 파일입니다.");
	}

	@Test
	void saveFiles_multipleFiles() throws IOException {
		MultipartFile file1 = new MockMultipartFile("file1", "a.png", "image/png", "aaa".getBytes());
		MultipartFile file2 = new MockMultipartFile("file2", "b.png", "image/png", "bbb".getBytes());

		List<String> urls = fileStorageService.saveFiles(List.of(file1, file2));

		assertThat(urls).hasSize(2);
		assertThat(Files.list(Paths.get(testUploadPath)).count()).isEqualTo(2);
	}

	// 리플렉션으로 private 필드 주입
	private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}
}