package com.spring.blog.common.Interceptors;

import com.spring.blog.exception.CoordinateConvertException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class WeatherInterceptor implements HandlerInterceptor {

    static final double RE = 6371.00877; // 지구 반경 (km)
    static final double GRID = 5.0;      // 격자 간격 (km)
    static final double SLAT1 = 30.0;    // 투영 위도1 (degree)
    static final double SLAT2 = 60.0;    // 투영 위도2 (degree)
    static final double OLON = 126.0;    // 기준점 경도 (degree)
    static final double OLAT = 38.0;     // 기준점 위도 (degree)
    static final double XO = 43;         // 기준점 X 좌표 (격자 기준)
    static final double YO = 136;        // 기준점 Y 좌표 (격자 기준)

    static final double LATITUDE_MIN = 33.2235722222222;
    static final double LATITUDE_MAX = 38.4916444444444;

    static final double LONGITUDE_MIN = 124.7141;
    static final double LONGITUDE_MAX = 131.8648471;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String latitudeStr = request.getParameter("latitude");
        String longitudeStr = request.getParameter("longitude");

        if (StringUtils.hasText(latitudeStr) && StringUtils.hasText(longitudeStr)) {

            try {
                double latitude = Double.parseDouble(latitudeStr);
                double longitude = Double.parseDouble(longitudeStr);

                checkRange(latitude, longitude);

                int[] coordinate = convert(latitude, longitude);

                request.setAttribute("nx", coordinate[0]);
                request.setAttribute("ny", coordinate[1]);

                return true;
            } catch (NumberFormatException ex) {
                throw new CoordinateConvertException("문자 입력으로 위경도 -> 좌표 변환 실패", ex);
            }
        } else {
            throw new CoordinateConvertException("위도와 경도를 입력해주세요.");
        }
    }

    private void checkRange(double latitude, double longitude) {
        if (latitude < LATITUDE_MIN || latitude > LATITUDE_MAX) {
            throw new CoordinateConvertException("유효 범위를 벗어난 위도");
        }
        if (longitude < LONGITUDE_MIN || longitude > LONGITUDE_MAX) {
            throw new CoordinateConvertException("유효 범위를 벗어난 경도");
        }
    }

    private int[] convert(double latitude, double longitude) {

        double DEGRAD = Math.PI / 180.0;
        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);

        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;

        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        double ra = Math.tan(Math.PI * 0.25 + latitude * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);

        double theta = longitude * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;

        int x = (int) Math.floor(ra * Math.sin(theta) + XO + 0.5);
        int y = (int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        return new int[]{x, y};
    }
}