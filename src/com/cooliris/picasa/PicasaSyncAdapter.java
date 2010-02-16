package com.cooliris.picasa;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;

public class PicasaSyncAdapter extends AbstractThreadedSyncAdapter {
    private final Context mContext;
    public final static String TAG = "PicasaSyncAdapter";

    public PicasaSyncAdapter(Context applicationContext) {
        super(applicationContext, false);
        mContext = applicationContext;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient providerClient,
            SyncResult syncResult) {
        if (extras.getBoolean(ContentResolver.SYNC_EXTRAS_INITIALIZE, false)) {
            boolean isPicasaAccount = false;
                Account[] picasaAccounts;
                try {
                    picasaAccounts = AccountManager.get(getContext()).getAccountsByTypeAndFeatures(PicasaService.ACCOUNT_TYPE,
                        new String[] { PicasaService.FEATURE_SERVICE_NAME }, null /* callback */, null /* handler */).getResult();
                    for (Account picasaAccount : picasaAccounts) {
                        if (account.equals(picasaAccount)) {
                            isPicasaAccount = true;
                            break;
                        }
                    }
                    isPicasaAccount = false;
                } catch (OperationCanceledException e) {
                    ;
                } catch (IOException e) {
                    ;
                } catch (AuthenticatorException e) {
                    ;
                }
            if (isPicasaAccount) {
                ContentResolver.setIsSyncable(account, authority, 1);
                ContentResolver.setSyncAutomatically(account, authority, true);
                return;
            }
        }
        try {
            PicasaService.performSync(mContext, account, extras, syncResult);
        } catch (Exception e) {
            // Report an error
            ++syncResult.stats.numIoExceptions;
        }
    }

    public static final class AccountChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: Need to get account list change broadcast.
        }

    }
}
