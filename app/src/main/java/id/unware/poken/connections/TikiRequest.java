package id.unware.poken.connections;

import id.unware.poken.pojo.PojoAreaData;
import id.unware.poken.pojo.PojoOfficeLocationData;
import id.unware.poken.pojo.PojoTariffCheck;
import id.unware.poken.pojo.PojoTrackingData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by marzellamega on 4/26/16.
 */
public interface TikiRequest {

    @GET(ConstantsRetrofit.ENDPOINT_TRACKING)
    Call<PojoTrackingData> getTracking(@Path("resi") String resi);

    @GET(ConstantsRetrofit.ENDPOINT_SEARCH_AREA)
    Call<PojoAreaData> searchArea(@Path("query") String query);

    //{origin}/{dest}/{weight}
    @GET(ConstantsRetrofit.ENDPOINT_CHECK_TARIFF)
    Call<PojoTariffCheck> checkTariff(@Path("origin") String origin,
                                      @Path("dest") String dest,
                                      @Path("weight") String weight);

    /**
     * Fetch nearby branches.
     *
     * @return PojoOfficeLocationData data
     */
    @GET(ConstantsRetrofit.ENDPOINT_FETCH_NEARBY_BRANCHES)
    Call<PojoOfficeLocationData> fetchNearbyBranchOffices(@Path("lat") double lat,
                                                          @Path("lon") double lon);
//
//    @FormUrlEncoded
//    @POST(ConstantsRetrofit.ENDPOINT_LOGIN)
//    Call<PojoLoginData> doLogin(@Field("emp_no") String operator, @Field("eqp_code") String alatBerat);
//
//    @FormUrlEncoded
//    @POST(ConstantsRetrofit.ENDPOINT_UPDATE_EQUIPMENT)
//    Call<PojoUpdateEquipmentData> updateEquipment(@Field("emp_no") String employee_numb,
//                                                  @Field("eqp_code") String eqp_code,
//                                                  @Field("lat") double lat,
//                                                  @Field("lon") double lon,
//                                                  @Field("status") String status,
//                                                  @Field("status_id") String status_id
//    );
//
//    @FormUrlEncoded
//    @POST(ConstantsRetrofit.ENDPOINT_LOGOUT)
//    Call<PojoLogout> doLogout(@Field("eqp_code") String eqp_code);
//
//    @FormUrlEncoded
//    @POST(ConstantsRetrofit.ENDPOINT_SEND_MESSAGE)
//    Call<PojoChatData> sendMessage(@Field("from") String from,
//                                   @Field("to") String to,
//                                   @Field("pit_id") String pitId,
//                                   @Field("msg") String message);
//
//    @FormUrlEncoded
//    @POST(ConstantsRetrofit.ENDPOINT_GET_CHAT)
//    Call<PojoGetChatData> getChat(@Field("to") String to,
//                                  @Field("from") String from,
//                                  @Field("id") String lastChatId);
//
//    @POST(ConstantsRetrofit.ENDPOINT_MAP_OBJECT)
//    Call<PojoMapObjectData> getAllMapObject();
}
