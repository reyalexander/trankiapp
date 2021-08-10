package com.example.guardian.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.guardian.models.ContactCelModel;
import com.example.guardian.models.ContactModel;
import com.example.guardian.models.SitesModel;
import com.example.guardian.models.TicketModel;

import java.io.File;
import java.util.ArrayList;

public class SqliteClass {

    /* @TABLE_APP_USER */
    public static final String TABLE_APP_USER = "app_user";

    /* @TABLE_APP_SITES */
    public static final String TABLE_APP_SITES = "app_sites";
    public static final String KEY_SITID = "as_id";
    public static final String KEY_SITNAM = "as_nam";
    public static final String KEY_SITPOSLAT= "as_pos_lati";
    public static final String KEY_SITPOSLON= "as_pos_long";
    public static final String KEY_SITDIR= "as_dir";
    public static final String KEY_SITCOR= "as_corr";
    public static final String KEY_SITRUC= "as_ruc";
    public static final String KEY_SITHOR= "as_horario";
    public static final String KEY_SITSTA= "as_state";

    /* @TABLE_APP_CONTACT */
    public static final String TABLE_APP_CONTACT = "app_contacts";
    public static final String KEY_CONTID = "ac_id";
    public static final String KEY_CONTIDAPI = "ac_id_api";
    public static final String KEY_CONTPOSLAT = "ac_pos_lati";
    public static final String KEY_CONTPOSLON = "ac_pos_long";
    public static final String KEY_CONTNAM = "ac_nam";
    public static final String KEY_CONTAPE = "ac_ape";
    public static final String KEY_CONTADR = "ac_adress";
    public static final String KEY_CONTCOR = "ac_corr";
    public static final String KEY_CONTDNI = "ac_dni";
    public static final String KEY_CONTCEL = "ac_cel";
    public static final String KEY_CONTSTA = "ac_state";

    /* @TABLE_APP_CONTACT_BY_CEL*/
    public static final String TABLE_APP_CONTACTBYCEL = "app_contacts_by_cel";
    public static final String KEY_CONTBYCELID = "ac_id_by_cel";
    public static final String KEY_CONTBYCELNAM = "ac_nam_by_cel";
    public static final String KEY_CONTBYCELCEL = "ac_cel_by_cel";

    /* @TABLE_APP_TICKET*/
    public static final String TABLE_APP_TICKET = "app_ticket";
    public static final String KEY_TICID = "at_id";
    public static final String KEY_TICCODDISP = "at_cod_disp";
    public static final String KEY_TICNUMOPE = "at_num_ope";
    public static final String KEY_TICFECH = "at_fecha";
    public static final String KEY_TICHORA = "at_hora";
    public static final String KEY_TICNOMCLI = "at_nom_cli";
    public static final String KEY_TICCELCLI = "at_cel_cli";
    public static final String KEY_TICDIR = "at_dir";
    public static final String KEY_TICLAT = "at_latitud";
    public static final String KEY_TICLONG = "at_longitud";
    public static final String KEY_TICURLMAP = "at_url_map";

    public DatabaseHelper databasehelp;
    private static SqliteClass SqliteInstance = null;

    private SqliteClass(Context context) {
        databasehelp = new DatabaseHelper(context);
    }

    public static SqliteClass getInstance(Context context) {
        if (SqliteInstance == null) {
            SqliteInstance = new SqliteClass(context);
        }
        return SqliteInstance;
    }

    public class DatabaseHelper extends SQLiteOpenHelper {
        public Context context;
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "app_tranki.db";

        public AppSitesSql siteSql;
        public AppContactSql contactSql;
        public AppContacByCeltSql contacByCeltSql;
        public AppTicketSql ticketSql;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            siteSql = new AppSitesSql();
            contactSql = new AppContactSql();
            contacByCeltSql = new AppContacByCeltSql();
            ticketSql = new AppTicketSql();
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            /* @TABLE_USER */

            /* @TABLE_SITES */
            String CREATE_TABLE_SITE = "CREATE TABLE " + TABLE_APP_SITES + "("
                    + KEY_SITID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SITNAM + " TEXT,"
                    + KEY_SITPOSLAT + " TEXT,"  + KEY_SITPOSLON + " TEXT," +KEY_SITDIR + " TEXT,"
                    + KEY_SITCOR + " TEXT," + KEY_SITRUC + " TEXT," + KEY_SITHOR + " TEXT," + KEY_SITSTA + " TEXT )";

            /* @TABLE_CONTACTS */
            String CREATE_TABLE_CONTACT = "CREATE TABLE " + TABLE_APP_CONTACT + "("
                    + KEY_CONTID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CONTIDAPI + " TEXT," + KEY_CONTPOSLAT + " TEXT,"
                    + KEY_CONTPOSLON + " TEXT,"  + KEY_CONTNAM + " TEXT," +KEY_CONTCOR + " TEXT," + KEY_CONTAPE + " TEXT," + KEY_CONTADR + " TEXT,"
                    + KEY_CONTDNI + " TEXT,"  + KEY_CONTCEL + " TEXT," + KEY_CONTSTA + " TEXT )";

            /* @TABLE_CONTACTS */
            String CREATE_TABLE_CONTACT_BY_CEL = "CREATE TABLE " + TABLE_APP_CONTACTBYCEL + "("
                    + KEY_CONTBYCELID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CONTBYCELNAM + " TEXT," + KEY_CONTBYCELCEL + " TEXT )";

            /* @TABLE_TICKET */
            String CREATE_TABLE_TICKET = "CREATE TABLE " + TABLE_APP_TICKET + "("
                    + KEY_TICID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TICCODDISP + " TEXT," + KEY_TICNUMOPE + " TEXT,"
                    + KEY_TICFECH + " TEXT," + KEY_TICHORA + " TEXT," + KEY_TICNOMCLI + " TEXT," + KEY_TICCELCLI + " TEXT,"
                    + KEY_TICLAT + " TEXT," + KEY_TICLONG + " TEXT," + KEY_TICDIR + " TEXT,"
                    + KEY_TICURLMAP + " TEXT )";

            /* @EXECSQL_CREATE */
            //db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_SITE);
            db.execSQL(CREATE_TABLE_CONTACT);
            db.execSQL(CREATE_TABLE_CONTACT_BY_CEL);
            db.execSQL(CREATE_TABLE_TICKET);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            /* @EXECSQL_DROP */
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_USER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_SITES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_CONTACT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_CONTACTBYCEL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_TICKET);

        }

        public boolean checkDataBase(){
            File dbFile = new File(context.getDatabasePath(DATABASE_NAME).toString());
            return dbFile.exists();
        }
        public void deleteDataBase(){
            context.deleteDatabase(DATABASE_NAME);
        }

        /* @CLASS_USERSQL */


        /* @CLASS_SITESSQL */
        public class AppSitesSql {
            public AppSitesSql() {
            }

            public void deleteSites() {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_SITES, null, null);
                db.close();
            }

            public void addSite(SitesModel site) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                //values.put(KEY_SITID, user.getId());
                values.put(KEY_SITNAM, site.getName());
                values.put(KEY_SITPOSLAT, site.getPos_lati());
                values.put(KEY_SITPOSLON, site.getPos_longi());
                values.put(KEY_SITDIR, site.getDireccion());
                values.put(KEY_SITCOR, site.getCorreo());
                values.put(KEY_SITRUC, site.getRUC());
                values.put(KEY_SITHOR, site.getHorario());
                values.put(KEY_SITSTA, site.getState());

                db.insert(TABLE_APP_SITES, null, values);
                db.close();
            }
            public SitesModel getSite(String id){
                SitesModel model = new SitesModel();
                String selectQuery = "SELECT * FROM " + TABLE_APP_SITES + " WHERE " + KEY_SITID + "='" + id + "'" ;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {

                    model.setId(cursor.getInt(cursor.getColumnIndex(KEY_SITID)));
                    model.setName(cursor.getString(cursor.getColumnIndex(KEY_SITNAM)));
                    model.setPos_lati(cursor.getString(cursor.getColumnIndex(KEY_SITPOSLAT)));
                    model.setPos_longi(cursor.getString(cursor.getColumnIndex(KEY_SITPOSLON)));
                    model.setDireccion(cursor.getString(cursor.getColumnIndex(KEY_SITDIR)));
                    model.setCorreo(cursor.getString(cursor.getColumnIndex(KEY_SITCOR)));
                    model.setRUC(cursor.getString(cursor.getColumnIndex(KEY_SITRUC)));
                    model.setHorario(cursor.getString(cursor.getColumnIndex(KEY_SITHOR)));
                    model.setState(cursor.getString(cursor.getColumnIndex(KEY_SITSTA)));

                }
                cursor.close();
                db.close();
                return model;
            }

            public ArrayList<SitesModel> getAllSites() {
                ArrayList<SitesModel> models = new ArrayList<SitesModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_SITES;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {

                    do{
                        SitesModel model = new SitesModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_SITID)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_SITNAM)));
                        model.setPos_lati(cursor.getString(cursor.getColumnIndex(KEY_SITPOSLAT)));
                        model.setPos_longi(cursor.getString(cursor.getColumnIndex(KEY_SITPOSLON)));
                        model.setDireccion(cursor.getString(cursor.getColumnIndex(KEY_SITDIR)));
                        model.setCorreo(cursor.getString(cursor.getColumnIndex(KEY_SITCOR)));
                        model.setRUC(cursor.getString(cursor.getColumnIndex(KEY_SITRUC)));
                        model.setHorario(cursor.getString(cursor.getColumnIndex(KEY_SITHOR)));
                        model.setState(cursor.getString(cursor.getColumnIndex(KEY_SITSTA)));

                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }

            public String getType(int id){
                String result = "";
                String selectQuery = "SELECT * FROM " + TABLE_APP_SITES +" WHERE "+ KEY_SITID + "='" + id + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    result = cursor.getString((cursor.getColumnIndex(KEY_SITSTA)));
                }
                cursor.close();
                db.close();
                return result;
            }
        }

        /* @CLASS_CONTACTSQL */
        public class AppContactSql {
            public void deleteContact() {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_CONTACT, null, null);
                db.close();
            }

            public void addContact(ContactModel contac) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                //values.put(KEY_SITID, user.getId());
                values.put(KEY_CONTIDAPI, contac.getId_cont_api());
                values.put(KEY_CONTPOSLAT, contac.getPos_lati_cont());
                values.put(KEY_CONTPOSLON, contac.getPos_longi_cont());
                values.put(KEY_CONTNAM, contac.getName_cont());
                values.put(KEY_CONTADR, contac.getAddrescont());
                values.put(KEY_CONTAPE, contac.getApelli_cont());
                values.put(KEY_CONTCOR, contac.getCorreo_cont());
                values.put(KEY_CONTDNI, contac.getDNI_cont());
                values.put(KEY_CONTCEL, contac.getCelular_cont());
                values.put(KEY_CONTSTA, contac.getState_cont());

                db.insert(TABLE_APP_CONTACT, null, values);
                db.close();
            }

            public ArrayList<ContactModel> getAllContacts() {
                ArrayList<ContactModel> models = new ArrayList<ContactModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_CONTACT;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        ContactModel model = new ContactModel();
                        model.setId_cont(cursor.getInt(cursor.getColumnIndex(KEY_CONTID)));
                        model.setId_cont_api(cursor.getInt(cursor.getColumnIndex(KEY_CONTIDAPI)));
                        model.setPos_lati_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTPOSLAT)));
                        model.setPos_longi_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTPOSLON)));
                        model.setName_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTNAM)));
                        model.setApelli_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTAPE)));
                        model.setAddrescont(cursor.getString(cursor.getColumnIndex(KEY_CONTADR)));
                        model.setCorreo_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTCOR)));
                        model.setDNI_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTDNI)));
                        model.setCelular_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTCEL)));
                        model.setState_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTSTA)));

                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                //db.close();
                return models;
            }


            public void updateLatiLongiContact(String lati,String longi, String contact){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values =new ContentValues();
                values.put(KEY_CONTPOSLAT,lati);
                values.put(KEY_CONTPOSLON,longi);
                db.update(TABLE_APP_CONTACT, values, KEY_CONTIDAPI + " = ?",new String[] { contact });
                //db.close();
            }

            public ContactModel getcontact(Integer id){
                ContactModel model = new ContactModel();
                String selectQuery = "SELECT * FROM " + TABLE_APP_CONTACT + " WHERE " + KEY_CONTID + "='" + id + "'" ;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {

                    model.setId_cont(cursor.getInt(cursor.getColumnIndex(KEY_CONTID)));
                    model.setPos_lati_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTPOSLAT)));
                    model.setPos_longi_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTPOSLON)));
                    model.setName_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTNAM)));
                    model.setCorreo_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTCOR)));
                    model.setDNI_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTDNI)));
                    model.setCelular_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTCEL)));
                    model.setState_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTSTA)));

                }
                cursor.close();
                db.close();
                return model;
            }

            public String getTypeCont(int id){
                String result = "";
                String selectQuery = "SELECT * FROM " + TABLE_APP_CONTACT +" WHERE "+ KEY_CONTID + "='" + id + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    result = cursor.getString((cursor.getColumnIndex(KEY_CONTSTA)));
                }
                cursor.close();
                db.close();
                return result;
            }
        }

        /* @CLASS_CONTACTBYCELSQL */
        public class AppContacByCeltSql {
            public void deleteContact() {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_CONTACTBYCEL, null, null);
                db.close();
            }

            public void addContactByCel(ContactCelModel contac) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();

                //values.put(KEY_SITID, contac.getId_cont());
                values.put(KEY_CONTBYCELNAM, contac.getName_cont());
                values.put(KEY_CONTBYCELCEL, contac.getCelular_cont());

                db.insert(TABLE_APP_CONTACTBYCEL, null, values);
                db.close();
            }

            public ArrayList<ContactCelModel> getAllContactsByCel() {
                ArrayList<ContactCelModel> models = new ArrayList<ContactCelModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_CONTACTBYCEL;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {

                    do{
                        ContactCelModel model = new ContactCelModel();
                        model.setId_cont(cursor.getInt(cursor.getColumnIndex(KEY_CONTBYCELID)));
                        model.setName_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTBYCELNAM)));
                        model.setCelular_cont(cursor.getString(cursor.getColumnIndex(KEY_CONTBYCELCEL)));

                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }
        }

        /* @CLASS_TICKET */
        public class AppTicketSql{

            public void deleteContact() {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_TICKET, null, null);
                db.close();
            }

            public void addTicket(TicketModel ticket) {
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                //values.put(KEY_SITID, ticket.getId());
                values.put(KEY_TICCODDISP, ticket.getCodDispositivo());
                values.put(KEY_TICNUMOPE, ticket.getNumOperacion());
                values.put(KEY_TICFECH, ticket.getFecha());
                values.put(KEY_TICHORA, ticket.getHora());
                values.put(KEY_TICNOMCLI, ticket.getCliNombre());
                values.put(KEY_TICCELCLI, ticket.getCliCelular());
                values.put(KEY_TICLAT, ticket.getLatitud());
                values.put(KEY_TICLONG, ticket.getLongitud());
                values.put(KEY_TICDIR, ticket.getDireccion());
                values.put(KEY_TICURLMAP, ticket.getUrlMap());

                db.insert(TABLE_APP_TICKET, null, values);
                db.close();
            }

            public void deleteTicketId(String id){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_TICKET , KEY_TICID + " = ?",
                        new String[] { String.valueOf(id) });
                db.close();
            }

            public int CountRowsTicket(){
                String selectQuery = "SELECT COUNT(*) FROM " + TABLE_APP_TICKET;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                int count = cursor.getCount();
                cursor.close();
                return count;
            }

            public ArrayList<TicketModel> getAllTicket() {
                ArrayList<TicketModel> models = new ArrayList<TicketModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_TICKET;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        TicketModel model = new TicketModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_TICID)));
                        model.setCodDispositivo(cursor.getString(cursor.getColumnIndex(KEY_TICCODDISP)));
                        model.setNumOperacion(cursor.getString(cursor.getColumnIndex(KEY_TICNUMOPE)));
                        model.setFecha(cursor.getString(cursor.getColumnIndex(KEY_TICFECH)));
                        model.setHora(cursor.getString(cursor.getColumnIndex(KEY_TICHORA)));
                        model.setCliNombre(cursor.getString(cursor.getColumnIndex(KEY_TICNOMCLI)));
                        model.setCliCelular(cursor.getString(cursor.getColumnIndex(KEY_TICCELCLI)));
                        model.setLatitud(cursor.getDouble(cursor.getColumnIndex(KEY_TICLAT)));
                        model.setLongitud(cursor.getDouble(cursor.getColumnIndex(KEY_TICLONG)));
                        model.setDireccion(cursor.getString(cursor.getColumnIndex(KEY_TICDIR)));
                        model.setUrlMap(cursor.getString(cursor.getColumnIndex(KEY_TICURLMAP)));
                        models.add(model);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
                return models;
            }

        }
    }

}
