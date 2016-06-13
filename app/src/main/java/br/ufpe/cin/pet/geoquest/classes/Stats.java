package br.ufpe.cin.pet.geoquest.classes;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Stats {

    public int rankingPos;
    public String favoriteArea;
    public int percentageCompleted;
    public Vector<PairAreaPercentage> areas = new Vector<PairAreaPercentage>();

    public static final String rankingPosKey = "posicao";
    public static final String favoriteAreaKey = "categoria";
    public static final String percentageCompletedKey = "porcentagem";
    public static final String areasKey = "categorias_porcentagem";

    public static final String[] CATEGORIES = {
            "Matemática", "História", "Português", "Geografia", "Inglês",
            "Sociologia", "Literatura", "Física", "Química", "Biologia"
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
        public int percentage;

        public PairAreaPercentage(String area, int percentage){
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

