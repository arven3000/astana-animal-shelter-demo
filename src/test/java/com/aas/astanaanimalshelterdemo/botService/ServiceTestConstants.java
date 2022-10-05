package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Avatar;
import com.aas.astanaanimalshelterdemo.botModel.CatUsers;
import com.aas.astanaanimalshelterdemo.botModel.DogUsers;
import com.aas.astanaanimalshelterdemo.botModel.Info;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import com.aas.astanaanimalshelterdemo.botModel.Report;
import com.aas.astanaanimalshelterdemo.botModel.Users;

import static com.aas.astanaanimalshelterdemo.botModel.AnimalType.CAT;
import static com.aas.astanaanimalshelterdemo.botModel.AnimalType.DOG;

public class ServiceTestConstants {
    public static final Pet JACK = new Pet(1L, "Jack", DOG, 1, null);
    public static final Pet MURKA = new Pet(2L, "Murka", CAT, 3, null);
    public static final Pet BOSS = new Pet(3L, "Boss", DOG, 2, null);

    public static final CatUsers IVAN = new CatUsers();
    public static final DogUsers ANNA = new DogUsers();
    public static final Users MAKSIM = new Users();

    public static final Report ONE = new Report();
    public static final Report TWO = new Report();
    public static final Report THREE = new Report();
    public static final Avatar JACK_AVATAR = new Avatar();
    public static final Avatar MURKA_AVATAR = new Avatar();
    public static final Avatar BOSS_AVATAR = new Avatar();
    public static final Info info = new Info();

}
