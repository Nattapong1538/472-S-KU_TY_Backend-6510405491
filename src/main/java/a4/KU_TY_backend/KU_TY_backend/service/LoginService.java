package a4.KU_TY_backend.KU_TY_backend.service;

import a4.KU_TY_backend.KU_TY_backend.entity.User;
import a4.KU_TY_backend.KU_TY_backend.repository.UserRepository;
import a4.KU_TY_backend.KU_TY_backend.request.LoginRequest;
import a4.KU_TY_backend.KU_TY_backend.util.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
@Service
public class LoginService {
    @Autowired
    private UserRepository repository;
    public User login(LoginRequest loginRequest){
        try {
            // ใช้ username ที่ยังไม่ encrypt เพื่อค้นหาและเพิ่ม User
            String username = loginRequest.getUsername();

            //username password encrypt เพื่อ login
            loginRequest.setPassword(Encryption.encrypt(loginRequest.getPassword()));
            loginRequest.setUsername(Encryption.encrypt(loginRequest.getUsername()));
            String appKey = "txCR5732xYYWDGdd49M3R19o1OVwdRFc";

            // สร้าง URL ของ API
            URL url = new URL("https://myapi.ku.th/auth/login");

            // เปิดการเชื่อมต่อ
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // ตั้งค่าเมธอด HTTP
            connection.setRequestMethod("POST");

            // ตั้งค่าหัวข้อ Content-Type
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("app-key", appKey);

            // เปิดให้ส่งข้อมูลออก
            connection.setDoOutput(true);

            // แปลง input เป็น JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonInputString = mapper.writeValueAsString(loginRequest);
            System.out.println(jsonInputString);
            // แปลง string เป็น byte
            byte[] postData = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // ส่งข้อมูล
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
            }

            // อ่านการตอบกลับ
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // อ่านข้อมูลการตอบกลับ
            BufferedReader reader;
            if (responseCode >= 200 && responseCode <= 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            // อ่านและเก็บ response
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // ปิดการเชื่อมต่อ
            connection.disconnect();

            // พิมพ์ผลลัพธ์
            System.out.println("Response: " + response.toString());

            // แปลง response เป็น JSON
            JSONObject jsonResponse = new JSONObject(response.toString());

            // return user
            String code = jsonResponse.get("code").toString();
            if (code.equals("success")) {
                User user = repository.findByUsername(username);
                if (user == null) {
                    user = new User();
                    user.setUsername(username);
                    return repository.save(user);
                }
                return user;
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
