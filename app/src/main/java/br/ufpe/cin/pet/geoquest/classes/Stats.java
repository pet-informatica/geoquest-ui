package br.ufpe.cin.pet.geoquest.classes;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Stats {

    public int rankingPos;
    public String favoriteArea;
    public double percentageCompleted;
    public Vector<PairAreaPercentage> areas = new Vector<PairAreaPercentage>();

    public static final String rankingPosKey = "position";
    public static final String favoriteAreaKey = "category";
    public static final String percentageCompletedKey = "percentage";
    public static final String areasKey = "categories";

    public static final String[] CATEGORIES = {
            "geologia", "hidrografia", "geografia_politica", "população", "urbanismo"
    };


    public Stats(){}

    public Stats(int rankingPos, String favoriteArea, int percentageCompleted,
                 Vector<PairAreaPercentage> areas) {
        this.rankingPos = rankingPos;
        this.favoriteArea = favoriteArea;
        this.percentageCompleted = percentageCompleted;
        this.areas = areas;
    }

    public static class PairAreaPercentage{

        public String area;
        public double percentage;

        public PairAreaPercentage(String area, double percentage){
            this.area = area;
            this.percentage = percentage;
        }
    }

    public void sortAreas(){
        Collections.sort(this.areas, new Comparator<PairAreaPercentage>() {
            @Override
            public int compare(PairAreaPercentage lhs, PairAreaPercentage rhs) {
                if(lhs.percentage == rhs.percentage) return lhs.area.compareToIgnoreCase(rhs.area);
                if(lhs.percentage < rhs.percentage) return 1;
                if(lhs.percentage > rhs.percentage) return -1;
                return  0;
            }
        });
    }

}

