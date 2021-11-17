package nextstep.subway.line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given, when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");
        
        // then
        // 지하철_노선_생성됨
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        // 지하철_노선_등록되어_있음
        지하철_노선_생성_요청("신분당선", "bg-red-600");

        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // then
        // 지하철_노선_생성_실패됨
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        LineResponse 신분당선 = 지하철_노선_생성_요청("신분당선", "bg-red-600").as(LineResponse.class);
        
        // 지하철_노선_등록되어_있음
        LineResponse 이호선 = 지하철_노선_생성_요청("2호선", "bg-red-600").as(LineResponse.class);

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        // 지하철_노선_목록_응답됨
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        
        // 지하철_노선_목록_포함됨
        List<Long> expected = new ArrayList<Long>();
        expected.add(신분당선.getId());
        expected.add(이호선.getId());
        List<Long> actual = new ArrayList<>(response.jsonPath().getList(".",LineResponse.class))
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        Assertions.assertThat(actual).containsAll(expected);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        LineResponse 신분당선 = 지하철_노선_생성_요청("신분당선", "bg-red-600").as(LineResponse.class);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response =  지하철_노선_조회_요청(신분당선.getId());

        // then
        // 지하철_노선_응답됨
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_수정_요청

        // then
        // 지하철_노선_수정됨
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_제거_요청

        // then
        // 지하철_노선_삭제됨
    }
    
    private ExtractableResponse<Response> 지하철_노선_생성_요청 (String name, String color) {
        Map<String, String> line = new HashMap<>();
        line.put("name", name);
        line.put("color", color);
        
        return RestAssured
                .given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all().extract();
    }
    
    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청 () {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all().extract();
    }
    
    private ExtractableResponse<Response> 지하철_노선_조회_요청 (Long id) {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines/{id}", id)
                .then().log().all().extract();
    }
}
