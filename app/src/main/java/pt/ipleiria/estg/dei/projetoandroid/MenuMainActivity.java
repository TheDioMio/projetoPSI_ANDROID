package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    public static final String IDUSER = "iduser";
    private int iduser;
    TextView nav_tvName, nav_tvEmail;
    ImageView nav_imgUser;


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
        carregarCabecalho(); //TODO:criar método

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        carregarFragmentoInicial();
    }

    private void carregarFragmentoInicial() {
       Fragment fragment = new HomeFragment();
       fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

    }

    private void carregarCabecalho() {
        iduser= getIntent().getIntExtra(IDUSER, 0);
        User user = AppSingleton.getInstance().getUser(iduser);

        View headerView = navigationView.getHeaderView(0);
        nav_tvEmail = headerView.findViewById(R.id.tvEmail);
        nav_tvName = headerView.findViewById(R.id.tvName);
        nav_imgUser = headerView.findViewById(R.id.imgUser);

        if (user != null){
            nav_tvName.setText(user.getName().toString());
            nav_tvEmail.setText(user.getEmail().toString());
            String avatarUrl = user.getImgAvatar();
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.logo_cores)
                        .error(R.mipmap.default_avatar)
                        .circleCrop()
                        .into(nav_imgUser);
            } else {
                nav_imgUser.setImageResource(R.mipmap.default_avatar);
            }
        }

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

            fragment = new ProfileFragment();
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav Detalhes Perfil");
        }

        if (fragment != null){
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


}