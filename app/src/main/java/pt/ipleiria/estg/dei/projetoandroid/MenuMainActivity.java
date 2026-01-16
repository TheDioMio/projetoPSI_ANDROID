package pt.ipleiria.estg.dei.projetoandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import pt.ipleiria.estg.dei.projetoandroid.listeners.MenuListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Me;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnItemSelectedListener,MenuListener {

    public static final int VIEW = 100;
    public static final int EDIT = 200;
    public static final String TOKEN = "token";

    public static final int ADD_ANIMAL = 10;
    public static final int EDIT_ANIMAL = 20;

    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String AVATAR = "avatar";
    public static final String IDUSER = "iduser";
    public static final String ADDRESS = "address";
    public static final String IMGAVATAR = "imgAvatar";
    private String token, avatar, email, name;
    private int iduser;
    private User userLogado;
    TextView nav_tvName, nav_tvEmail;
    ImageView nav_imgUser;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;

    private BottomNavigationView  bottomNav;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ActionBarDrawerToggle drawerToggle;
    private static final int REQ_NOTIF = 1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        requestNotificationPermissionIfNeeded();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        bottomNav = findViewById(R.id.bottomNav);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.ndOpen,
                R.string.ndClose
        );

        // Listener correto para o ícone (hamburger/seta)
        drawerToggle.setToolbarNavigationClickListener(v -> {
            if (!drawerToggle.isDrawerIndicatorEnabled()) {
                // Estamos no modo seta → faz back
                getOnBackPressedDispatcher().onBackPressed();
            } else {
                // Estamos no modo hamburger → abre/fecha drawer
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        carregarCabecalho();

        navigationView.setNavigationItemSelectedListener(this);
        bottomNav.setOnItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        FragmentManager fm = getSupportFragmentManager();
                        if (fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        } else {
//                            carregarFragmentoInicial();  // volta a colocar o HomeFragment
//                            showHamburger();
                            moveTaskToBack(true);
                        }
                    }
                });

        carregarFragmentoInicial();

    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQ_NOTIF
                );
            }
        }
    }

    //METODOS PARA TRATAR DA SETA E DO HAMBURGUER
    public void showHamburger() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
    }

    public void showBackArrow() {
        drawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



//    public void abrirFragment(Fragment fragment, boolean addToBackStack) {
//        FragmentTransaction ft = getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.contentFragment, fragment);
//
//        if (addToBackStack) {
//            ft.addToBackStack(null);
//        }
//
//        ft.commit();
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_NOTIF) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notificações ativadas", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notificações desativadas (não vais receber alertas)", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void navegarPara(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFragment, fragment)
                .addToBackStack(null)
                .commit();

        showBackArrow();
    }


    private void clearBackStack() {
        getSupportFragmentManager().popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
        );
    }


    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }



    private void carregarFragmentoInicial() {
        fragmentManager.beginTransaction()
                .replace(R.id.contentFragment, new HomeFragment())
                //.addToBackStack("HOME")
                .commit();

        showHamburger();
//       Fragment fragment = new HomeFragment();
//       fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

    }

    private void carregarCabecalho() {


        //DEVE IR BUSCAR O TOKEN A SHARED PREFERENCES E NÃO AO INTENT
        // 1️⃣ Receber token do intent
//        token = getIntent().getStringExtra(TOKEN);
//
//        if (token == null) {
//            Toast.makeText(this, "Token em falta", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // 2️⃣ Guardar token nas SharedPreferences
//        SharedPreferences sp =
//                getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
//
//        sp.edit().putString("TOKEN", token).apply();

        // 3️⃣ Configurar Singleton
        AppSingleton singleton = AppSingleton.getInstance(getApplicationContext());
        singleton.setMenuListener(this);

        // 4️⃣ Chamar API (AGORA o token já existe)
        singleton.getMe(this);

        // 5️⃣ Inicializar views do header
        View headerView = navigationView.getHeaderView(0);
        nav_tvEmail = headerView.findViewById(R.id.tvEmail);
        nav_tvName = headerView.findViewById(R.id.tvName);
        nav_imgUser = headerView.findViewById(R.id.imgUser);
    }


    public User getUserLogado() {
        //TEM DE IR A SHARED PREFERENCES BUSCAR O USER LOGADO
        return userLogado;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.navHome || id == R.id.bottom_home) {
//            navegarPara(new HomeFragment(), true);
//            getSupportFragmentManager().popBackStack(
//                    "HOME",
//                    FragmentManager.POP_BACK_STACK_INCLUSIVE
//            );

            getSupportFragmentManager().popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
            );
            carregarFragmentoInicial();

        } else if (id == R.id.navDetalhesAnimal || id == R.id.bottom_animals) {
            navegarPara(new AllAnimalsFragment());

        } else if (id == R.id.navMyAnimals) {
            navegarPara(new MyAnimalsFragment());

        } else if (id == R.id.navCandidaturas || id == R.id.bottom_application) {
            navegarPara(new ApplicationMenuFragment());

        } else if (id == R.id.navMessage || id == R.id.bottom_messages) {
            navegarPara(new MessageListFragment());

        } else if (id == R.id.navProfileDetails || id == R.id.bottom_profile) {
            startActivityForResult(
                    new Intent(this, ProfileActivity.class),
                    EDIT
            );
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onRefreshMenu(Me me) {

        if (me == null) {
            Toast.makeText(this, "Erro ao carregar dados do utilizador", Toast.LENGTH_SHORT).show();
            return;
        }

        //DEVE IR A SHARED PREFERENCES BUSCAR OS DADOS DO USER LOGADO
        sharedPreferences = getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        //definir o Editor para permitir guardar / alterar o valor
        editor = sharedPreferences.edit();
        if (me.getName()!=null || me.getUsername() != null || me.getEmail() != null || me.getAddress() != null|| me.getImgAvatar()!= null || me.getId() > 0) {
            if ( me.getName()!= null) {
                editor.putString(NAME, me.getName());
            }
            if ( me.getUsername()!= null) {
                editor.putString(USERNAME, me.getUsername());
            }
            if ( me.getEmail()!= null) {
                editor.putString(EMAIL, me.getEmail());
            }
            if ( me.getAddress()!= null) {
                editor.putString(ADDRESS, me.getAddress());
            }
            if ( me.getImgAvatar()!= null) {
                editor.putString(IMGAVATAR, me.getImgAvatar());
            }
            if ( me.getId() > 0) {
                editor.putString(IDUSER, String.valueOf(me.getId()));
            }

            editor.apply();
        }

            nav_tvName.setText(me.getName());
            nav_tvEmail.setText(me.getEmail());

        editor.putInt("USER_ID_INT", me.getId());
        editor.apply();
        iduser = me.getId();

        String avatar = me.getImgAvatar();

        if (avatar != null && !avatar.isEmpty()) {

            String imageUrl;

            if (avatar.startsWith("http")) {
                imageUrl = avatar;
            } else {
                imageUrl = AppSingleton.getInstance(getApplicationContext()).getEndereco() + AppSingleton.getInstance(getApplicationContext()).FRONTEND_BASE_URL + avatar;
            }

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.mipmap.default_avatar)
                    .error(R.mipmap.default_avatar)
                    .circleCrop()
                    .into(nav_imgUser);

        } else {
            nav_imgUser.setImageResource(R.mipmap.default_avatar);
        }

        SharedPreferences sp = getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);

        int myId = sp.getInt("USER_ID_INT", -1);
        if (myId > 0) {
            String topic = "users/" + myId + "/NEW_MESSAGE";
            try {
                MqttManager.getInstance(getApplicationContext()).connectAndSubscribe(topic);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao iniciar MQTT: " + e, Toast.LENGTH_LONG).show();
            }
        }

        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK){
            atualizarMenuFromSharedPreferences();
//          ATUALIZAR O MENU COM OS DADOS DA SHAREDPREFERENCES
            if (requestCode == MenuMainActivity.EDIT){
                Toast.makeText(getApplicationContext(), "Dados atualizados", Toast.LENGTH_LONG).show();
            } else if (requestCode == MenuMainActivity.VIEW){
                Toast.makeText(getApplicationContext(), "aqui vem do view do user", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void atualizarMenuFromSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);

        String name = sp.getString(NAME, "");
        String email = sp.getString(EMAIL, "");
        String avatar = sp.getString(IMGAVATAR, "");

        nav_tvName.setText(name);
        nav_tvEmail.setText(email);

        if (avatar != null && !avatar.isEmpty()) {
            String imageUrl = avatar.startsWith("http")
                    ? avatar
                    : AppSingleton.getInstance(getApplicationContext()).getEndereco() + AppSingleton.getInstance(this).FRONTEND_BASE_URL + avatar;

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.mipmap.default_avatar)
                    .circleCrop()
                    .into(nav_imgUser);
        }
    }

}