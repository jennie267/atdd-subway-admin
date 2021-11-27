package nextstep.subway.line;

import static nextstep.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    
    @DisplayName("기존 구간에 새 지하철 구간을 추가한다")
    @Test
    void addSection() {
        // given
        StationResponse 서울대입구역 = 지하철역_등록되어_있음("서울대입구역");
        StationResponse 낙성대역 = 지하철역_등록되어_있음("낙성대역");
        LineResponse 이호선 = 지하철_노선_등록되어_있음(LineRequest.of("이호선", "bg-green-600", 서울대입구역.getId(), 낙성대역.getId(), 30));
        
        StationResponse 봉천역 = 지하철역_등록되어_있음("봉천역");
        SectionRequest request = SectionRequest.of(봉천역.getId(), 서울대입구역.getId(), 15);

        // when
        ExtractableResponse<Response> response = 구간_추가_요청(이호선.getId(), request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 구간_추가_요청(Long lineId, SectionRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{id}/sections", lineId)
                .then().log().all()
                .extract();
    }

}