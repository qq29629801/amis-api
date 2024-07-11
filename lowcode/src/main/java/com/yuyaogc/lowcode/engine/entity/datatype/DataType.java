package com.yuyaogc.lowcode.engine.entity.datatype;

import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Query;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.Validate;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.ColumnType;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Config;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

public class DataType {

    static Map<String, Class> typeFields = new HashMap<>();

    static {
        registerField(Constants.BINARY, ByteField.class);
        registerField(Constants.STRING, StringField.class);
        registerField(Constants.BIG_DECIMAL, BigDecimalField.class);
        registerField(Constants.DOUBLE, DoubleField.class);
        registerField(Constants.LONG, LongField.class);
        registerField(Constants.BOOLEAN, BooleanField.class);
        registerField(Constants.DATE, DateField.class);
        registerField(Constants.DATETIME, DateTimeField.class);
        registerField(Constants.TIMESTAMP, TimestampField.class);
        registerField(Constants.FLOAT, FloatField.class);
        registerField(Constants.INTEGER, IntegerField.class);
        registerField(Constants.INT, IntegerField.class);
        registerField(Constants.TEXT, TextField.class);
        registerField(Constants.MANY2MANY, Many2manyField.class);
        registerField(Constants.MANY2ONE, Many2oneField.class);
        registerField(Constants.ONE2MANY, One2manyField.class);
        registerField(Constants.CLASS, ClassField.class);
    }

    public static Set<String> getFieldTypes() {
        return typeFields.keySet();
    }

    public static String getFieldName(String type) {
        return typeFields.get(type).getName();
    }

    public List<String> read(EntityField field, Query query){
        return null;
    }

    public String quote(String identify) {
        return String.format("`%s`", identify);
    }


    public String getName() {
        Optional<Map.Entry<String, Class>> optional = typeFields.entrySet().stream().filter(d -> d.getValue().equals(this.getClass())).findFirst();
        return optional.isPresent() ? optional.get().getKey() : Constants.CLASS;
    }



    public <T extends Model> T write(Model v, EntityField field){
        return null;
    }


    public Object read(Model v,EntityField field){
        return null;
    }


    public static DataType create(String type) {
        Class<?> clazz = typeFields.get(type);
        if (clazz == null) {
            clazz = typeFields.get("Class");
        }
        try {
            DataType dataType = (DataType) clazz.getDeclaredConstructors()[0].newInstance(DataType.class.newInstance());
            return dataType;
        } catch (Exception e) {
            throw new EngineException(String.format("创建字段%s失败,class=%s", type, clazz.getName()));
        }
    }

    public static void registerField(String type, Class<?> fieldClass) {
        typeFields.put(type, fieldClass);
    }

    public ColumnType getType() {
        return null;
    }


    public boolean validate(EntityField entityField, Model value) {
        return true;
    }

    public Integer getSize(EntityField field) {
        return null;
    }


    public Integer getScale(EntityField field) {
        return null;
    }

    public Object convertToColumn(Object value, EntityClass record, boolean validate) {
        return value;
    }


    public class BigDecimalField extends DataType {

        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getBigDecimal(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Float;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }


    public class BooleanField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getBoolean(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Boolean;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 1;
        }
    }

    public class ByteField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getByte(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Binary;
        }
    }

    public class CharField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getShort(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.VarChar;
        }
    }


    public class DateField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getDate(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Date;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }
    public class TimestampField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getTimestamp(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.DateTime;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }
    public class DateTimeField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getLocalDateTime(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.DateTime;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class DoubleField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getDouble(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Decimal;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 10;
        }
    }

    public class FloatField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getFloat(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Float;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class IntegerField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getBigInteger(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Integer;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class LongField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getLong(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Long;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class Many2manyField extends DataType {
        @Override
        public <T extends Model> T write(Model v, EntityField field) {
            Object value =  v.get(field.getName());
            String valueString =   Objects.toString(value);
            Context context = Context.getInstance();
            Config config = context.getConfig();
            StringBuilder sql = new StringBuilder();
            List<Object> paras = new ArrayList();

            EntityClass table = Container.me().getEntityClass(field.getRelModel2());
            Map<String, Object> attrs = new LinkedHashMap<>();


            config.dialect.forModelSave(table, attrs, sql, paras);
          if(StringUtils.isNotEmpty(valueString)){
            String[] ids =  valueString.split(",");



          }

            Connection conn = null;
            PreparedStatement pst = null;
            boolean var8;
            try {
                conn = config.getConnection();
                if (config.getSqlDialect().isOracle()) {
                    pst = conn.prepareStatement(sql.toString(), table.getPrimaryKey());
                } else {
                    pst = conn.prepareStatement(sql.toString(), 1);
                }
                config.dialect.fillStatement(pst, paras);
                int result = pst.executeUpdate();
                //config.dialect.getModelGeneratedKey(this, pst, table);
                var8 = result >= 1;
            } catch (Exception var12) {
                throw new EngineException(var12);
            } finally {
                config.close(pst, conn);
            }

          return null;
        }


        @Override
        public Object read(Model v,EntityField field) {
            return null;
        }

        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }
        @Override
        public ColumnType getType() {
            return ColumnType.Long;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class Many2oneField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Long;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }



        @Override
        public List<String> read(EntityField field, Query query) {
            List<String> relColumns = new ArrayList<>();

            EntityClass rec =  field.getEntity();
            DataType.Many2oneField m2o = (DataType.Many2oneField) field.getDataType();
            String relModel = field.getRelModel();
            EntityClass relClass = Container.me().getEntityClass(relModel);
            String aliasRel = query.leftJoin(rec.getTableName(), field.getColumnName(), relClass.getTableName(), "id", field.getColumnName());

            for(EntityField relField: relClass.getFields()){
                if(relField.getName().equals("id")){
                    continue;
                }
                String alisColumn = String.format("%s.%s", aliasRel, quote(relField.getColumnName()));
                relColumns.add(String.format("%s as %s", alisColumn, quote(relField.getName())));
            }

            return relColumns;
        }
    }


    public class One2manyField extends DataType {
        @Override
        public <T extends Model> T write(Model v, EntityField field) {

            return null;
        }

        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }
        @Override
        public ColumnType getType() {
            return ColumnType.Long;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class StringField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (StringUtils.isEmpty(value.getStr(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.VarChar;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 255;
        }
    }

    public class TextField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }

        @Override
        public Integer getSize(EntityField field) {
            return null;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Text;
        }
    }

    public class ClassField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Text;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

}
