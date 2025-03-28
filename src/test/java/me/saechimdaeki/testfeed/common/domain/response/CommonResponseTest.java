package me.saechimdaeki.testfeed.common.domain.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommonResponseTest {

	@Test
	@DisplayName("공통 응답 생성 시 지정한 httpStatusCode는 정의된 Code에 매칭되어 반환해야 한다")
	void commonResponseStatusCodeTest() {
		// given
		int statusCode = 200;
		String data = "TestData";
		// when

		CommonResponse<String> commonResponse = CommonResponse.of(statusCode, data);

		// then

		assertThat(commonResponse.data).isEqualToIgnoringCase(data);
		assertThat(commonResponse.resultCode).isEqualTo(CommonResponse.Status.SUCCESS.getMessage());

	}

	@Test
	@DisplayName("정의되지 않은 코드거나 httpStatusCode의 경우 기본적으로 Success를 반환한다")
	void defaultStatusTest() {
		// given
		int statusCode = 999;
		// when
		String testMessage = CommonResponse.Status.getSpecificMessage(statusCode);

		// then
		assertThat(testMessage).isEqualTo(CommonResponse.Status.SUCCESS.getMessage());
	}
}