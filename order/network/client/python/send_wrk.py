import subprocess
from config import TARGET_URL, THREADS, CONNECTIONS, DURATION, NETNS

def run_wrk():
    cmd = [
        "sudo", "ip", "netns", "exec", NETNS,
        "wrk",
        f"-t{THREADS}",
        f"-c{CONNECTIONS}",
        f"-d{DURATION}",
        TARGET_URL
    ]

    try:
        result = subprocess.run(cmd, capture_output=True, text=True, check=True)
        print("WRK Output:")
        print(result.stdout)

        # Optional: Parse RPS or latency from stdout
        for line in result.stdout.splitlines():
            if "Requests/sec" in line or "Latency" in line:
                print("➤", line)

except subprocess.CalledProcessError as e:
print("❌ Error running wrk:")
print(e.stderr

if __name__ == "__main__":
    run_wrk()
