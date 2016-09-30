package com.ankit.pointofsolution.utility;

import com.ankit.pointofsolution.Models.Productdata;

/**
 * Created by Ankit on 13-Sep-16.
 */
public interface GetResponseDialogListener {

    void updateResult(boolean response);
    void updateResultSkuCode(Productdata response);
}
