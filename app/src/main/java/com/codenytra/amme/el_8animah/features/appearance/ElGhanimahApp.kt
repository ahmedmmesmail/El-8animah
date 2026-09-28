package com.codenytra.amme.el_8animah.features.appearance

import android.app.Application

class ElGhanimahApp : Application() {

    // lateinit = بنعلن المتغير بس مش بنعطيه قيمة دلوقتي
    // هيتعمل initialize في onCreate قبل أي Activity تشتغل
    lateinit var themeViewModel: ThemeViewModel
        private set  // private set = من برا الكلاس يقدروا يقروا بس مش يغيروا

    override fun onCreate() {
        super.onCreate()

        // بنعمل الـ ViewModel هنا بدل داخل Activity
        // ليه؟ عشان ThemeViewModel يحتاج ApplicationContext
        // والـ Application نفسها هي applicationContext
        themeViewModel = ThemeViewModel(this)

        // نحفظ instance في companion object عشان أي كود يوصله بسهولة
        instance = this
    }

    companion object {
        // instance = مؤشر على الـ Application الحالية
        // lateinit لأنها بتتعمل في onCreate مش عند التعريف
        lateinit var instance: ElGhanimahApp
            private set
    }
}