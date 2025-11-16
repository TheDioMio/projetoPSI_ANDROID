package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private User userLogado;
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
        userLogado = AppSingleton.getInstance().getUser(iduser);

        View headerView = navigationView.getHeaderView(0);
        nav_tvEmail = headerView.findViewById(R.id.tvEmail);
        nav_tvName = headerView.findViewById(R.id.tvName);
        nav_imgUser = headerView.findViewById(R.id.imgUser);

        if (userLogado != null){
            nav_tvName.setText(userLogado.getName().toString());
            nav_tvEmail.setText(userLogado.getEmail().toString());


            String imgName = userLogado.getImgAvatar();

            if (imgName != null && !imgName.isEmpty()) {
                // carregar a imagem da net
                if (imgName.startsWith("http")) {
                    Glide.with(this)
                            .load(imgName)
                            .placeholder(R.mipmap.default_avatar)
                            .error(R.mipmap.default_avatar)
                            .circleCrop()
                            .into(nav_imgUser);

                } else {
                    // carregar a imagem local
                    int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());

                    Glide.with(this)
                            .load(resId)
                            .placeholder(R.mipmap.default_avatar)
                            .error(R.mipmap.default_avatar)
                            .circleCrop()            // 🔥 Deixa redonda
                            .into(nav_imgUser);
                }

            } else {
                nav_imgUser.setImageResource(R.mipmap.default_avatar);
            }


        }

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

            fragment = new ProfileFragment();
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav Detalhes Perfil");
        }else if (menuItem.getItemId() == R.id.navMyAnimals) {

            fragment = new MyAnimalsFragment();
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav MY Animals");
        }


        if (fragment != null){
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


}