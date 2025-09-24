package com.ezequieldiaz.vacunatorioapp4.util;


import android.os.Parcel;

import com.google.android.material.datepicker.CalendarConstraints;

import java.util.Calendar;
import java.util.TimeZone;

public class ValidadorDeFinesDeSemanaYFechasPasadas implements CalendarConstraints.DateValidator {

    public static final Creator<ValidadorDeFinesDeSemanaYFechasPasadas> CREATOR =
            new Creator<ValidadorDeFinesDeSemanaYFechasPasadas>() {
                @Override
                public ValidadorDeFinesDeSemanaYFechasPasadas createFromParcel(Parcel in) {
                    return new ValidadorDeFinesDeSemanaYFechasPasadas();
                }

                @Override
                public ValidadorDeFinesDeSemanaYFechasPasadas[] newArray(int size) {
                    return new ValidadorDeFinesDeSemanaYFechasPasadas[size];
                }
            };

    @Override
    public boolean isValid(long date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(date);

        // 1. No permitir fechas pasadas
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (date < today.getTimeInMillis()) {
            return false;
        }

        // 2. Bloquear sábados y domingos
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Nada extra que guardar
    }
}
