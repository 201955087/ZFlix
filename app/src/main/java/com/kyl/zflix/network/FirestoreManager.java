package com.kyl.zflix.network;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class FirestoreManager {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // 작업 성공/실패 콜백 인터페이스
    public interface FavoriteActionCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    // 상태 확인 콜백 인터페이스
    public interface FavoriteStatusCallback {
        void onResult(boolean isFavorite);
    }

    // 즐겨찾기 목록 조회 콜백 인터페이스
    public interface FavoriteFetchCallback {
        void onFavoritesFetched(List<Favorite> favorites);
        void onFailure(Exception e);
    }

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void addFavoriteToFirestore(String propertyType, String propertyId, FavoriteActionCallback callback) {
        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (currentUserId == null) {
            callback.onFailure(new Exception("사용자 ID를 가져올 수 없습니다."));
            return;
        }

        Favorite newFavorite = new Favorite(currentUserId, propertyType, propertyId);

        // ADD를 사용하여 고유한 문서 ID를 생성합니다. (데이터 덮어쓰기 방지)
        db.collection("favorites")
                .add(newFavorite)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FirestoreManager", "즐겨찾기 추가 성공! 문서 ID: " + documentReference.getId());
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.w("FirestoreManager", "즐겨찾기 추가 실패", e);
                    callback.onFailure(e);
                });
    }

    public void removeFavoriteFromFirestore(String propertyType, String propertyId, FavoriteActionCallback callback) {
        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (currentUserId == null) {
            callback.onFailure(new Exception("사용자 ID를 가져올 수 없습니다."));
            return;
        }

        db.collection("favorites")
                .whereEqualTo("uid", currentUserId)
                .whereEqualTo("type", propertyType)
                .whereEqualTo("listing_id", propertyId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // 즐겨찾기는 고유해야 하므로, 첫 번째 찾은 항목을 삭제합니다.
                        DocumentReference docRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                        docRef.delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("FirestoreManager", "즐겨찾기 삭제 성공!");
                                    callback.onSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    Log.w("FirestoreManager", "즐겨찾기 삭제 실패", e);
                                    callback.onFailure(e);
                                });
                    } else {
                        callback.onFailure(new Exception("삭제할 즐겨찾기 항목을 찾을 수 없습니다."));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("FirestoreManager", "즐겨찾기 삭제 쿼리 실패", e);
                    callback.onFailure(e);
                });
    }

    /**
     * 수정: limit(1)을 제거하여 상태 확인의 무결성을 높였습니다.
     */
    public void isFavorite(String propertyType, String propertyId, FavoriteStatusCallback callback) {
        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (currentUserId == null) {
            callback.onResult(false);
            return;
        }

        db.collection("favorites")
                .whereEqualTo("uid", currentUserId)
                .whereEqualTo("type", propertyType)
                .whereEqualTo("listing_id", propertyId)
                // .limit(1) // 제거
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean isFav = !queryDocumentSnapshots.isEmpty();
                    Log.d("FirestoreManager", "즐겨찾기 상태 확인: " + isFav);
                    callback.onResult(isFav);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreManager", "즐겨찾기 상태 확인 실패", e);
                    callback.onResult(false);
                });
    }

    public void getFavoritesForCurrentUser(FavoriteFetchCallback callback) {
        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (currentUserId == null) {
            callback.onFailure(new Exception("사용자 ID를 가져올 수 없습니다."));
            return;
        }

        // 현재 사용자의 모든 즐겨찾기 항목을 가져옵니다.
        db.collection("favorites")
                .whereEqualTo("uid", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Favorite> favoriteList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Favorite favorite = document.toObject(Favorite.class);
                            favoriteList.add(favorite);
                        }
                        Log.d("FirestoreManager", "총 " + favoriteList.size() + "개의 즐겨찾기를 가져왔습니다.");
                        callback.onFavoritesFetched(favoriteList);
                    } else {
                        Log.w("FirestoreManager", "즐겨찾기 가져오기 실패", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }
}