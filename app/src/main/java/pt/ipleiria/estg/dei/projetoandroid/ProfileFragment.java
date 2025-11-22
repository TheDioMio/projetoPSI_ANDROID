package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  //  private static final String ARG_PARAM1 = "param1";
  //  private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
  //  private String mParam1;
 //   private String mParam2;
    private static final String ARG_USER_ID  = "user_id";
    private static final String ARG_IS_OWN  = "is_own";
    private int userId;
    private boolean isOwnProfile;


    // Valodação da nova password

    private EditText etNewPass;
    private EditText etRptNewPass;
    private EditText etName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etContact;
    private View layoutPasswordSection;
    private View btnSaveProfile;
    private View btnChangePhoto;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isOwnProfile Parameter 1.
     * @param userId Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int userId, Boolean isOwnProfile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putBoolean(ARG_IS_OWN, isOwnProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
            isOwnProfile = getArguments().getBoolean(ARG_IS_OWN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName       = view.findViewById(R.id.et_name);
        etUsername   = view.findViewById(R.id.et_username);
        etEmail      = view.findViewById(R.id.et_email);
        etContact    = view.findViewById(R.id.et_contact);
        etNewPass    = view.findViewById(R.id.et_newPassword);
        etRptNewPass = view.findViewById(R.id.et_rpt_new_password);

        layoutPasswordSection = view.findViewById(R.id.layoutPasswordSection);
        btnSaveProfile        = view.findViewById(R.id.btnSave);
        btnChangePhoto        = view.findViewById(R.id.imgProfileChange);

        User user = AppSingleton.getInstance().getUser(userId);

        if (user != null) {
            etName.setText(user.getName());
            etUsername.setText(user.getUsername());
            etEmail.setText(user.getEmail());
            etContact.setText(user.getAddress());  // alterar para contacto ou localização
        }

        if (!isOwnProfile) {
            // esconder coisas que só fazem sentido para o próprio utilizador
            if (layoutPasswordSection != null) {
                layoutPasswordSection.setVisibility(View.GONE);
            }
            if (btnSaveProfile != null) {
                btnSaveProfile.setVisibility(View.GONE);
            }
            if (btnChangePhoto != null) {
                btnChangePhoto.setVisibility(View.GONE);
            }

            // tornar campos só de leitura
            makeReadOnly(etName);
            makeReadOnly(etUsername);
            makeReadOnly(etEmail);
            makeReadOnly(etContact);

        } else {
            // modo "meu perfil": aqui ligas listeners de guardar, alterar pass, etc.
            // btnSaveProfile.setOnClickListener(...);
        }
    }

    private void makeReadOnly(EditText editText) {
        if (editText == null) return;
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setClickable(false);
        editText.setLongClickable(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackground(null);
    }
    private boolean validatePassword() {


        String newPassword = etNewPass.getText().toString().trim();
        String rptPassword = etRptNewPass.getText().toString().trim();

        if (!newPassword.equals(rptPassword)) {
            etRptNewPass.setError("As passwords não coincidem");
            etRptNewPass.requestFocus();
            return false;
        }


        return true;

    }


}