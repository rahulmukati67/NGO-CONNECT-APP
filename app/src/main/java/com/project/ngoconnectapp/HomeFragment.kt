import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.ngoconnectapp.NGOAdapter
import com.project.ngoconnectapp.NGODetailPage
import com.project.ngoconnectapp.Ngo_data
import com.project.ngoconnectapp.R

class HomeFragment : Fragment() {

    private lateinit var ngoAdapter: NGOAdapter
    private lateinit var searchView: SearchView
    private var ngoList = arrayListOf<Ngo_data>()
    private var filteredNgoList = ArrayList<Ngo_data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvNGOs = view.findViewById<RecyclerView>(R.id.rvNGOs)
        rvNGOs.layoutManager = LinearLayoutManager(context)
        rvNGOs.setHasFixedSize(true)

        val database = Firebase.database
        val dbRef = database.getReference("ngoDetails")

        ngoAdapter = NGOAdapter(ngoList)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ngoList.clear()
                if (snapshot.exists()) {
                    for (ngoSnap in snapshot.children) {
                        val ngoData = ngoSnap.getValue(Ngo_data::class.java)
                        ngoList.add(ngoData!!)
                    }
                    ngoAdapter.updateList(ngoList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })

        ngoAdapter.setItemClickListener(object : NGOAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                val intent = Intent(activity, NGODetailPage::class.java)
                intent.putExtra("name", ngoList[position].name)
                intent.putExtra("type", ngoList[position].ngoType)
                intent.putExtra("phoneNo", ngoList[position].phoneNo)
                intent.putExtra("email", ngoList[position].emailId)
                intent.putExtra("website", ngoList[position].ngoWeb)
                intent.putExtra("image", ngoList[position].ngoImage)
                startActivity(intent)
            }
        })

        rvNGOs.adapter = ngoAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterNgoList(newText)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun filterNgoList(query: String?) {
        filteredNgoList.clear()
        if (!query.isNullOrBlank()) {
            for (ngo in ngoList) {
                if (ngo.name?.toLowerCase()?.contains(query.toLowerCase()) == true) {
                    filteredNgoList.add(ngo)
                }
            }
        } else {
            filteredNgoList.addAll(ngoList)
        }

        ngoAdapter.updateList(filteredNgoList)
    }
}
