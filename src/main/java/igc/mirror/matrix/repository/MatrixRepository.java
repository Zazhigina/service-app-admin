package igc.mirror.matrix.repository;

import igc.mirror.auth.UserDetails;
import igc.mirror.matrix.model.Matrix;
import jooqdata.tables.TMatrix;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatrixRepository {
    static final Logger logger = LoggerFactory.getLogger(MatrixRepository.class);

    private static final TMatrix T_MATRIX = TMatrix.T_MATRIX;
    private final DSLContext dsl;
    private final UserDetails user;

    @Autowired
    public MatrixRepository(DSLContext dsl, UserDetails user) {
        this.dsl = dsl;
        this.user = user;
    }

    public List<Matrix> getMatrixInfoByAllParams(String company_code, String org_code, String customer_code, String initiator_code) {

        return dsl.select(TMatrix.T_MATRIX.fields())
                .from(TMatrix.T_MATRIX)
                .where(TMatrix.T_MATRIX.COMPANY_CODE.equal(company_code))
                .and(TMatrix.T_MATRIX.ORG_CODE.equal(org_code))
                .and(TMatrix.T_MATRIX.CUSTOMER_CODE.equal(customer_code))
                .and(TMatrix.T_MATRIX.INITIATOR_CODE.equal(initiator_code))
                .fetchInto(Matrix.class);
    }

    public void insertMatrixInfo(Matrix matrix) {
            dsl.insertInto(TMatrix.T_MATRIX)
                    .set(TMatrix.T_MATRIX.COMPANY_CODE, matrix.getCompanyCode())
                    .set(TMatrix.T_MATRIX.ORG_CODE, matrix.getOrgCode())
                    .set(TMatrix.T_MATRIX.CUSTOMER_CODE, matrix.getCustomerCode())
                    .set(TMatrix.T_MATRIX.INITIATOR_CODE, matrix.getInitiatorCode())
    //                .set(TMatrix.T_MATRIX.CREATE_DATE, matrix.getCreateDate())
                    .set(TMatrix.T_MATRIX.CREATE_USER, matrix.getCreateUser())
    //                .set(TMatrix.T_MATRIX.LAST_UPDATE_DATE, matrix.getLastUpdateDate())
    //                .set(TMatrix.T_MATRIX.LAST_UPDATE_USER, matrix.getLastUpdateUser())
                    .execute();
    }

    public List<Matrix> getInfoByCompanyCode(String company_code){
        return dsl.select(TMatrix.T_MATRIX.fields())
                .from(TMatrix.T_MATRIX)
                .where(TMatrix.T_MATRIX.COMPANY_CODE.equal(company_code))
                .fetchInto(Matrix.class);
    }

    public void deleteByCompanyCode(String company_code){
        dsl.deleteFrom(TMatrix.T_MATRIX)
                .where(TMatrix.T_MATRIX.COMPANY_CODE.equal(company_code))
                .execute();
    }

    public void deleteByObject(String company_code, String org_code, String customer_code, String initiator_code){
        dsl.deleteFrom(TMatrix.T_MATRIX)
                .where(TMatrix.T_MATRIX.COMPANY_CODE.equal(company_code))
                .and(TMatrix.T_MATRIX.ORG_CODE.equal(org_code))
                .and(T_MATRIX.CUSTOMER_CODE.equal(customer_code))
                .and(T_MATRIX.INITIATOR_CODE.equal(initiator_code))
                .execute();
    }

    public List<Matrix> getAllObjects(){
        return dsl.selectFrom(TMatrix.T_MATRIX)
                .fetchInto(Matrix.class);
    }

}
