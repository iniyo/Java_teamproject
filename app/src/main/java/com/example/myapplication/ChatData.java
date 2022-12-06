package com.example.myapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatData {
    private String msg;
    private String nickname;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

// 파이어베이스 문서에서 user UID와 일치하는 문서를 찾으면 해당 문서의 nick을 가져옴.
// 유저 정보 읽어옴
         /*
private final FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어store 인스턴스 선언
    private FirebaseAuth firebaseAuth;
   db.collection("UserInfo") //컬렉션 게시물 선택
                .get() // 데이터 가져옴
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserAccount userinfo = document.toObject(UserAccount.class); // 오브젝트 형식으로 변환.
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // 유저 UID랑 일치하면
                            if(userinfo.equals(user.getUid())){
                                nick = userinfo.getNickname();
                                Toast.makeText(chContext, userinfo.getNickname(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Toast.makeText(chContext, "유저 UID가 존재하지 않습니다. 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });*/