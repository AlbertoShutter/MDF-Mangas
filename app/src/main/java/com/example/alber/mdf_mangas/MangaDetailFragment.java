package com.example.alber.mdf_mangas;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alber.mdf_mangas.dummy.DummyContent;

/**
 * A fragment representing a single Manga detail screen.
 * This fragment is either contained in a {@link MangaListActivity}
 * in two-pane mode (on tablets) or a {@link MangaDetailActivity}
 * on handsets.
 */
public class MangaDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MangaDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.nombre);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.manga_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.rellenoGenero)).setText("Es del genero "+mItem.editorial+".");
            ((TextView) rootView.findViewById(R.id.rellenoEditorial)).setText("Pertence a la editorial "+mItem.genero+".");
            ((TextView) rootView.findViewById(R.id.rellenoTomos)).setText("Tiene un total de "+mItem.tomos+" tomos.");
            ((TextView) rootView.findViewById(R.id.rellenoAutor)).setText("Su mangaka es "+mItem.autor+".");
            ((TextView) rootView.findViewById(R.id.rellenoSipnosis)).setText(mItem.sipnosis);
        }
        return rootView;
    }
}
