package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Represent {@code /apis/mobile/wallet_info} APIs. <br/>
 * Created by marzellaalfamega on 2/2/17.
 *
 * @since Feb 3rd, 17 - [V49] NEW
 */
public class PojoWalletData extends PojoBase {

    @SerializedName("bank_list") private ArrayList<PojoBank> bankList;
    @SerializedName("deposit") private PojoDepositPending deposit;
    @SerializedName("updated_on") private String updatedOn;
    @SerializedName("user_balance") private String userBalance;
    @SerializedName("user_bank") private ArrayList<PojoUserBank> userBank;
    @SerializedName("user_transaction") private ArrayList<PojoUserTransaction> userTransaction;
    @SerializedName("withdraw") private PojoWithdrawPending withdraw;

    /**
     * [V49]
     * Wallet config to handle Deposit amount
     * or minimum withdrawal.
     */
    @SerializedName("config") private PojoWalletConfig walletConfig;


    public PojoWalletData() {
    }

    public ArrayList<PojoBank> getBankList() {
        return bankList;
    }

    public void setBankList(ArrayList<PojoBank> bankList) {
        this.bankList = bankList;
    }

    public PojoDepositPending getDeposit() {
        return deposit;
    }

    public void setDeposit(PojoDepositPending deposit) {
        this.deposit = deposit;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(String userBalance) {
        this.userBalance = userBalance;
    }

    public ArrayList<PojoUserBank> getUserBank() {
        return userBank;
    }

    public void setUserBank(ArrayList<PojoUserBank> userBank) {
        this.userBank = userBank;
    }

    public ArrayList<PojoUserTransaction> getUserTransaction() {
        return userTransaction;
    }

    public void setUserTransaction(ArrayList<PojoUserTransaction> userTransaction) {
        this.userTransaction = userTransaction;
    }

    public PojoWithdrawPending getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(PojoWithdrawPending withdraw) {
        this.withdraw = withdraw;
    }

    public PojoWalletConfig getWalletConfig() {
        return walletConfig;
    }

    public void setWalletConfig(PojoWalletConfig walletConfig) {
        this.walletConfig = walletConfig;
    }
}