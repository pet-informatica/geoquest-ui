package br.ufpe.cin.pet.geoquest.classes;

import java.util.Vector;

public class Stats {

    public int rankingPos;
    public String favoriteArea;
    public int percentageCompleted;
    public Vector<PairAreaPercentage> areas;

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

}

