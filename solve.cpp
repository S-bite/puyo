#include <bits/stdc++.h>
#define _overload3(_1, _2, _3, name, ...) name
#define _rep(i, n) repi(i, 0, n)
#define repi(i, a, b) for (int i = (a); i < (b); ++i)
#define rep(...) _overload3(__VA_ARGS__, repi, _rep, )(__VA_ARGS__)
#define ALL(x) x.begin(), x.end()
#define chmax(x, y) x = max(x, y)
#define chmin(x, y) x = min(x, y)
using namespace std;
std::random_device rnd;
std::mt19937 mt(rnd());
using ll = long long;
using lld = long double;
using VI = vector<int>;
using VVI = vector<VI>;
using VL = vector<ll>;
using VVL = vector<VL>;
using PII = pair<int, int>;
const int IINF = 1 << 30;
const ll INF = 1ll << 60;
const ll MOD = 1000000007;
int height;
int width;
int num;
VI puyo;

class State
{
public:
    ll score;
    VI order;
    VVI board;
    int turn;
    State(){};
    ~State(){};
};
bool operator<(const State &s1, const State &s2)
{
    return s1.score < s2.score;
}

void printVVI(VVI &board)
{
    for (auto l : board)
    {
        for (auto x : l)
        {
            cerr << x << " ";
        }
        cerr << endl;
    }
}

bool removePuyo(State &state, int y, int x, int curChain)
{
    bool isChanged = false;
    int dy[4] = {0, 0, 1, -1};
    int dx[4] = {1, -1, 0, 0};
    int color = state.board[y][x];
    if (color == -1)
        return false;

    deque<PII> que;
    que.push_back({y, x});
    vector<PII> chainPuyo;
    map<PII, bool> visited;
    while (!que.empty())
    {
        PII cur = que.front();
        que.pop_front();
        if (visited[cur])
            continue;
        visited[cur] = true;
        chainPuyo.push_back(cur);
        rep(i, 4)
        {
            int ny = cur.first + dy[i];
            int nx = cur.second + dx[i];
            if (0 <= ny && ny < height && 0 <= nx && nx < width && state.board[ny][nx] == color)
            {
                que.push_back({ny, nx});
            }
        }
    }
    if (chainPuyo.size() >= 4)
    {
        //printVVI(state.board);
        // /cerr << y << " " << x << endl;
        for (auto pos : chainPuyo)
        {
            state.board[pos.first][pos.second] = -1;
        }
        //printVVI(state.board);
        //exit(1);
        //state.score += 1000 * (int)pow(2, curChain - 1) + 100 * (chainPuyo.size() - 4);

        isChanged = true;
    }
    return isChanged;
}

void update(State &state)
{
    bool isChanged = false;
    int curChain = 1;
    do
    {
        isChanged = false;
        bool isFalling = false;
        do
        {
            isFalling = false;
            for (int i = height - 1; i >= 1; i--)
            {
                rep(j, width)
                {
                    if (state.board[i][j] == -1 && state.board[i - 1][j] != -1)
                    {
                        state.board[i][j] = state.board[i - 1][j];
                        state.board[i - 1][j] = -1;
                        isFalling = true;
                    }
                }
            }
        } while (isFalling);
        rep(i, height) rep(j, width)
        {
            isChanged |= removePuyo(state, i, j, curChain);
        }
        if (isChanged)
            curChain++;
    } while (isChanged);
    curChain--;
    if (curChain == 0)
        return;
    if (curChain >= 8)
    {
        state.score += (int)pow(3, curChain);
    }
    else
    {

        // state.score += 1;
    }
}

State chokudaiSearch(State &state)
{
    int beamwidth = num;
    vector<priority_queue<State>> choque(beamwidth + 1);
    choque[0].push(state);
    time_t start = clock();
    int loop = 0;
    while (clock() - start < CLOCKS_PER_SEC * 10.0)
    {
        rep(i, beamwidth)
        {
            if (choque[i].empty())
            {
                continue;
            }
            auto cur = choque[i].top();
            choque[i].pop();
            int r = mt() % width;
            rep(k, width)
            {
                int j = (k + r) % width;
                auto next = cur;
                next.turn++;
                if (next.board[0][j] != -1)
                    continue;
                next.board[0][j] = puyo[i];
                next.order.push_back(j);
                //cerr << "update" << endl;
                update(next);
                //cerr << "done" << endl;
                choque[i + 1].push(next);
            }
        }
        loop++;
    }
    //cerr << "ok" << endl;
    cerr << "loop:" << loop << endl;
    cerr << "score" << choque[beamwidth].top().score << endl;
    return choque[beamwidth].top();
}

int main()
{
    cin >> num >> height >> width;
    puyo.resize(num);
    rep(i, num)
    {
        cin >> puyo[i];
        cerr << "load " << i << "/" << num << endl;
    }
    State base;
    base.board = VVI(height, VI(width, -1));
    base.score = 0;
    base.turn = 0;
    State best = chokudaiSearch(base);
    for (auto i : best.order)
    {
        cout << i << endl;
    }
    return 0;
}