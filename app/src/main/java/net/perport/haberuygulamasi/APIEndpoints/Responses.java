package net.perport.haberuygulamasi.APIEndpoints;

import net.perport.haberuygulamasi.APIEndpoints.Models.Haber;
import net.perport.haberuygulamasi.APIEndpoints.Models.Notification;
import net.perport.haberuygulamasi.APIEndpoints.Models.PostYorum;
import net.perport.haberuygulamasi.APIEndpoints.Models.Yorum;

import java.util.List;

public class Responses {

    public class BaseResponse {
        public final boolean success = true;
    }
    public class ErrorResponse {
        public final boolean success = false;
        public int statusCode;
        public String message;
    }

    public class LoginResponse extends BaseResponse{
        public Body body;
        public class Body {
            public String refresh_token;
        }
    }
    public class SignupResponse extends BaseResponse{
        public String body;
    }

    public class RefreshTokenResponse extends BaseResponse{
        public Body body;
        public class Body {
            public String access_token;
        }
    }

    public class GetHaberResponse extends BaseResponse {
        public Body body;
        public class Body {
            public List<Haber> items;
            public int remaining;
        }
    }

    public class GetHaberSearchResponse extends BaseResponse {
        public List<Body> body;
        public class Body extends Haber {
            public String confidenceScore;
        }
    }

    public class GetSingleHaberResponse extends BaseResponse {
        public Body body;
        public class Body {
            public String _id;
            public String icerik;
        }
    }

    public class PostAccountResponse extends BaseResponse {
        public String body;
    }

    public class GetCommentsResponse extends BaseResponse {
        public List<Yorum> body;
    }

    public class PostCommentResponse extends BaseResponse {
        public PostYorum body;
    }

    public class GetNotificationsResponse extends BaseResponse {
        public Body body;
        public class Body {
            public long time;
            public List<Notification> items;
        }
    }
}
