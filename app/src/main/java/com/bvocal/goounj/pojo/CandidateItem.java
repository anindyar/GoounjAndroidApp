package com.bvocal.goounj.pojo;

import java.io.Serializable;

/**
 * Created by nandagopal on 13/1/16.
 */
public class CandidateItem implements Serializable {

    public int candidateId;
    public String name;
    public String nickName;
    public String about;
    public String manifesto;

    public CandidateItem(int candidateId, String name, String nickName, String about, String manifesto) {
        this.candidateId = candidateId;
        this.name = name;
        this.nickName = nickName;
        this.about = about;
        this.manifesto = manifesto;
    }
}
