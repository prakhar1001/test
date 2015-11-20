package prakhar1001.com.contactmanager_optimustest17nov.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactFragment;
import prakhar1001.com.contactmanager_optimustest17nov.FavouriteFragment.FavouriteFragment;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.GroupFragment;
import prakhar1001.com.contactmanager_optimustest17nov.R;
import prakhar1001.com.contactmanager_optimustest17nov.SearchActivity.SearchActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Button Search = (Button) findViewById(R.id.Search_query);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(i);
            }
        });

    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ContactFragment(), "Contacts");
        adapter.addFrag(new FavouriteFragment(), "Favourites");
        adapter.addFrag(new GroupFragment(), "Groups");
        viewPager.setAdapter(adapter);
    }
}