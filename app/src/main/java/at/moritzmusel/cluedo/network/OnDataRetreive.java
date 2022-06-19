package at.moritzmusel.cluedo.network;

import com.google.firebase.database.DataSnapshot;

public interface OnDataRetreive {
    void onSuccess(DataSnapshot dataSnapshot);
    void onFailure(Object error);
}
