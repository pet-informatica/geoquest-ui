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
    public static final String areaNameKey = "name";
    public static final String areaPercKey = "perc";
    public static final String level1Key = "1";
    public static final String level2Key = "2";
    public static final String level3Key = "3";

    public static final String[] CATEGORIES = {
            "Cultura, Sociedade e Globalização",
            "Geografia do Brasil",
            "Demografia",
            "Geopolítica e Geoeconomia",
            "Geografia Urbana",
            "Geografia Agrária",
            "Meio Ambiente e Recursos Naturais",
            "Geografia Física e Fenômenos Naturais",
            "Cartografia"
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
        public double l1;
        public double l2;
        public double l3;

        public PairAreaPercentage(String area, double percentage, double l1, double l2, double l3){
            this.area = area;
            this.percentage = percentage;
            this.l1 = l1;
            this.l2 = l2;
            this.l3 = l3;
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

