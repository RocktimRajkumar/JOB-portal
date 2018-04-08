package brdevelopers.com.jobvibe;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Edit_EducationFragment extends Fragment {

    private EditText et_university,et_college,et_cyoc,et_cper;
    private EditText et_12board,et_12school,et_12yoc,et_12per;
    private EditText et_10board,et_10school,et_10yoc,et_10per;
    private TextView tv_btnnext;
    private ProgressBar progressBar;

    private final String editEducation="http://103.230.103.142/jobportalapp/job.asmx/EditCandidateEducationalDetails";
    private final String editPersonal="http://103.230.103.142/jobportalapp/job.asmx/EditCandidatePersonalDetails";
    private String getcandidatedetail="http://103.230.103.142/jobportalapp/job.asmx/GetCandidateDetails";

    private CandidateDetails candidateDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit__education,container,false);
        Bundle bundle=getArguments();
        candidateDetails= (CandidateDetails) bundle.getSerializable("candidateDetails");
        return view;
    }

}
