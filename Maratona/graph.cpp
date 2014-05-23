#include <utility>
#include <vector>
#include <queue>
#include <climits>
#include <set>
#include <cstdio>


#define branco 0
#define cinza 1
#define preto 2

using namespace std;

//peso, destino
typedef pair<int, int> edge;
typedef vector<vector<edge> > graph;


void insertG(graph &g, int v1, int v2, int p){
	g[v1].push_back(make_pair(p, v2));
}

void vdfs(graph &g, int v, vector<int>& t, vector<int>& d, vector<int>& c, int &tempo, vector<int>& antec){
	c[v] = cinza;
	tempo++;
	d[v] = tempo;
	for(int i = 0; i<g[v].size(); i++){
		int u = g[v][i].second;
		if(c[u] == branco){
			antec[u] = v;
			vdfs(g, u, t, d, c, tempo, antec);
		}
	}
	c[v] = preto;
	tempo++;
	t[v] = tempo;
}

void dfs(graph &g){
	int tempo = 0;
	vector<int> t(g.size()), d(g.size()), c(g.size(), branco), antec(g.size(), -1);
	for(int i = 0; i<g.size(); i++){
		if(c[i] == branco)
			vdfs(g, i, t, d, c, tempo, antec);
	}
}

void vbfs(graph& g, int v, vector<int>& dist, vector<int>& c, vector<int>& antec){
	c[v] = cinza;
	dist[v] = 0;
	queue<int> fila;
	fila.push(v);
	while(!fila.empty()){
		int u = fila.front();
		fila.pop();
		for(int i = 0; i<g[u].size(); i++){
			int w = g[u][i].second;
			if(c[w] == branco){
				c[w] = cinza;
				dist[w] = dist[u] + 1;
				antec[w] = u;
				fila.push(w);
			}
		}
		c[u] = preto;
	}
	
}

vector<int> bfs(graph& g){
	vector<int> dist(g.size(), INT_MAX);
	vector<int> c(g.size(), branco);
	vector<int> antec(g.size(), -1);
	for(int i = 0; i<g.size(); i++){
		if(c[i] == branco){
			vbfs(g, i, dist, c, antec);
		}
	}
	
	return antec;
} 


vector<int> daiquistra(graph& g, int v){
	vector<int> dist(g.size(), INT_MAX);
	vector<int> prev(g.size(), -1);
	
	dist[v] = 0;
	set<pair<int, int> > q;
	for(int i = 0; i<g.size(); i++){
		q.insert(make_pair(dist[i], i));
	}
	
	while(!q.empty()){
		int u = q.begin()->second;
		int p = q.begin()->first;
		q.erase(q.begin());
		if(dist[u] == INT_MAX)
			break;
		
		for(int i = 0; i<g[u].size(); i++){
			int w = g[u][i].second;
			int alt = dist[u] + g[u][i].first;
			if(alt < dist[w]){
				q.erase(make_pair(dist[w], w));
				dist[w] = alt;
				prev[w] = u;
				q.insert(make_pair(dist[w], w));
			}
		}
	}
	
	return prev;	
}

int main(){
	graph g(5);
	insertG(g,0,1,1);
	insertG(g,1,2,1);
	insertG(g,2,3,1);
	insertG(g,3,4,1);
	insertG(g,4,0,10);
	insertG(g,1,0,1);
	insertG(g,2,1,1);
	insertG(g,3,2,1);
	insertG(g,4,3,1);
	insertG(g,0,4,10);
	vector<int> a = daiquistra(g, 0);
	int dest = 4;
	int orig = 0;
	while(dest != orig){
		printf("%d <-- ", dest);
		dest = a[dest];
	}
	printf("%d\n", dest);
}
