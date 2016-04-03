package com.bvocal.goounj.pojo;

/**
 * Created by nandagopal on 13/1/16.
 */
public class CandidateItem {

    public int candidateId;
    public String name;
    public String nickName;
    public String about;
    public String manifesto;

//    public String candidateName, candidateAbout;
//    public int candidateImage;
//
//    public CandidateItem(String candidateName, String candidateAbout, int candidateImage) {
//        this.candidateName = candidateName;
//        this.candidateAbout = candidateAbout;
//        this.candidateImage = candidateImage;
//    }


    public CandidateItem(int candidateId, String name, String nickName, String about, String manifesto) {
        this.candidateId = candidateId;
        this.name = name;
        this.nickName = nickName;
        this.about = about;
        this.manifesto = manifesto;
    }
}
