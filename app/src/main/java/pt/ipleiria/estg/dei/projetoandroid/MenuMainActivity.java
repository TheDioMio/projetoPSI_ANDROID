package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import pt.ipleiria.estg.dei.projetoandroid.listeners.MenuListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Me;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MenuListener {

    public static final String TOKEN = "token";

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String AVATAR = "avatar";
    public static final String IDUSER = "iduser";
    private String token, avatar, email, name;
    private int iduser;
    private User userLogado;
    TextView nav_tvName, nav_tvEmail;
    ImageView nav_imgUser;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        carregarCabecalho();

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        carregarFragmentoInicial();
    }





    private void carregarFragmentoInicial() {
       Fragment fragment = new HomeFragment();
       fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

    }

    private void carregarCabecalho() {

        // 1️⃣ Receber token do intent
        token = getIntent().getStringExtra(TOKEN);

        if (token == null) {
            Toast.makeText(this, "Token em falta", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2️⃣ Guardar token nas SharedPreferences
        SharedPreferences sp =
                getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);

        sp.edit().putString("TOKEN", token).apply();

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
        return userLogado;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        if (menuItem.getItemId() == R.id.navHome){
            fragment = new HomeFragment();
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav Home");
        } else if (menuItem.getItemId() == R.id.navDetalhesAnimal){
            //fragment = new DinamicoFragment();
            //setTitle(menuItem.getTitle());
            System.out.println("--> Nav Dinamico");
        } else if (menuItem.getItemId() == R.id.navProfileDetails) {

            fragment = ProfileFragment.newInstance(iduser, false);
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav Detalhes Perfil");
        }else if (menuItem.getItemId() == R.id.navMyAnimals) {

            fragment = new MyAnimalsFragment();
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav MY Animals");
        } else if (menuItem.getItemId()== R.id.navCandidaturas) {
            fragment = new ApplicationListFragment();
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav Listagem de Candidaturas");
        } else if (menuItem.getItemId() == R.id.navMessage) {

            fragment = new MessageListFragment(); // ou MessageListFragment.newInstance()
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav Message List");
        }

        if (fragment != null){
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    @Override
    public void onRefreshMenu(Me me) {

            nav_tvName.setText(me.getName());
            nav_tvEmail.setText(me.getEmail());


        String avatar = me.getImgAvatar();

        if (avatar != null && !avatar.isEmpty()) {

            String imageUrl;

            if (avatar.startsWith("http")) {
                imageUrl = avatar;
            } else {
                imageUrl = AppSingleton.getInstance(getApplicationContext()).FRONTEND_BASE_URL + avatar;
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


        }


}